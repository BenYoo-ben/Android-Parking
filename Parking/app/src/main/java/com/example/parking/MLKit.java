package com.example.parking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.Arrays;
import java.util.List;

public class MLKit {

    //계정의 고유키값
    private final static String CLOUD_VISION_API_KEY = "AIzaSyChepJug-3t_kNjXPmEU4ku9Evon1dWy9o";
    private String rString;
    private MLKit mlk_copy;
    private EditText[] VPC = new EditText[3];
    private long imagecode;


    public long getImageCode()
    {
        return this.imagecode;
    }
    public void setImageCode(long ic)
    {
        this.imagecode= ic;
    }

    protected void getVPC(EditText[] VPC)
    {
        this.VPC = VPC;
    }
    protected void changeOnString(String rs)
    {
        //결과값 스트링을 파싱하여 어느항목에 추가해야되는지 확인
        if(rs==null) return;
        else {
            if (rs.charAt(0) == 'V') {
                VPC[0].setText(rs.substring(2, rs.length()));
            }
            if (rs.charAt(0) == 'P') {
                VPC[1].setText(rs.substring(2, rs.length()));
            }
        }
    }

    Context A;
    public MLKit(Context A)
    {
this.A = A;
    }
    public MLKit(MLKit mlk,Context A)
    {
        this.VPC = mlk.VPC;
        this.mlk_copy =mlk;
        this.A = A;
    }
    Task<FirebaseVisionText> result;

    void runTextRecognition(Bitmap image_bitmap)
    {
        //문자인식 함수 시작.
        String returningString;
        FirebaseVisionImage FVI = FirebaseVisionImage.fromBitmap(image_bitmap);
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("ko"))
                .build();
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getCloudTextRecognizer(options);




      result =  recognizer.processImage(FVI)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {

                                Log.d("RECOGNITION","Successful");

                                if((rString = parseString(resultText(result,A)))!=null)
                                {
                                    Toast.makeText(A,"인식성공,"+rString,Toast.LENGTH_LONG);
                                    Log.d("RECOGNITION", rString);
                                    changeOnString(rString);
                                    mlk_copy.setImageCode(getImageCode());
                                }
                                else{
                                    Toast.makeText(A,"인식실패, 다시시도해주세요.",Toast.LENGTH_LONG);
                                    Log.d("RECOGNITION","String=null, screwed");
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception

                                Log.d("RECOGNITION","Failure");
                                e.printStackTrace();
                            }
                        });



    }
    //문자인식후 인식된 모든문자를 String 값ㄷ에 정렬
    String resultText(Task<FirebaseVisionText> result, Context A)
    {
        String resultText="";
        if(result.isSuccessful()) {
            Log.d("RECOGNITION","showText working!");
            resultText = result.getResult().getText();
            for (FirebaseVisionText.TextBlock block : result.getResult().getTextBlocks()) {
                String blockText = block.getText();
                Float blockConfidence = block.getConfidence();
                List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                for (FirebaseVisionText.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Float lineConfidence = line.getConfidence();
                    List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for (FirebaseVisionText.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Float elementConfidence = element.getConfidence();
                        List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
                    }
                }
            }
            Log.d("RECOGNITION","RESULT IS\n"+resultText);
            return resultText;
        }
        Log.d("RECOGNITION","showText F Failed");

return null;

    }
    //정렬된 String을 통해서 어느 항목에 속하거나 필요없는 문자열인지 구분하는 함수
    String parseString(String s)
    {
        // 0 for vehicle num, 1 for phone
        if(s==null){
            Log.d("RECOGNITION","Parsing Fail");
            return null;
        }
        String[] results = s.split("\n");
        for(int i=0;i<results.length;i++)
        {
            Log.d("RECOGNITION","["+i+"] --> "+results[i]);
            results[i] = results[i].replaceAll(" ","");

            results[i] = results[i].replaceAll("-","");

            results[i] = results[i].replaceAll("•","");
            results[i] = results[i].replaceAll("\\.","");
            Log.d("RECOGNITION","["+i+"] --> "+results[i]);
            {
                Log.d("RECOGNITION",results[i]+"Phone no.\n");
                return "P:"+results[i];
            }
            if(isVehicle(results[i])) {
                Log.d("RECOGNITION", results[i] + "Vehicle no.\n");
                return "V:"+results[i];
            }

        }
            return null;
    }
    //차량번호에 해당하는 문자열인지 확인해주는 함수
    private boolean isVehicle(String s)
    {
        char c;
        int charcount= 0;
        if(s.length() > 8 || s.length() <7) return false;

        for(int i=0;i<s.length();i++)
      {
          if(charcount>1) return false;
          c=s.charAt(i);

          if(c<48 || c>57)
          {
              charcount++;
          }
      }
    if(charcount==0) return false;
        return true;
    }


}

package com.example.parking;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.List;
import java.util.Vector;

public class MLKit {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyChepJug-3t_kNjXPmEU4ku9Evon1dWy9o";
    String rString;
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
    Task<FirebaseVisionText> result;

    void runTextRecognition(Bitmap image_bitmap)
    {
        String returningString;
        FirebaseVisionImage FVI = FirebaseVisionImage.fromBitmap(image_bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getCloudTextRecognizer();
      result =  recognizer.processImage(FVI)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {

                                Log.d("RECOGNITION","Successful");

                                rString = parseString(resultText(result,A));
                                Log.d("RECOGNITION",rString);
                                changeOnString(rString);
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
    String parseString(String s)
    {
        // 0 for vehicle num, 1 for phone

        String[] results = s.split("\n");
        for(int i=0;i<results.length;i++)
        {
            results[i] = results[i].replaceAll(" ","");
            results[i] = results[i].replaceAll("-","");

            if(results[i].contains("010"))
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

    public void sendDatatoIntent(Intent intent,Vector<String>[] vectors)
    {
        intent.putExtra("VehicleNums",vectors[0]);
        intent.putExtra("PhoneNums",vectors[1]);
        A.startActivity(intent);
    }


}

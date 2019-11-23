package com.example.parking;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

public class OpenCVProcess {
    static Bitmap BitmapOpenCV;
    Bitmap image1;
    Mat img1;

    private void Preprocess_Bitmap()
    {


    }



     private void Recognition()
     {
         /*
         ImageView imageViewROI1;
         ImageView imageViewROI2;
         ImageView imageViewROI3;
         ImageView imageViewROI4;
         ImageView imageViewROI5;
         ImageView imageViewROI6;
         ImageView imageViewROI7;
         ImageView imageViewROI8;
         ImageView imageViewROI9;


       int[][][] ones = new int[10][15][10];
         int[][][] nums = new int[10][15][10];
         OpenCVLoader.initDebug();
         Utils.bitmapToMat(BitmapOpenCV,img1);

         Mat mat_gray = new Mat();
         Mat mat_cny = new Mat();

         Imgproc.cvtColor(img1,mat_gray,COLOR_BGR2GRAY);
         Imgproc.threshold(mat_gray,mat_cny,160,255,Imgproc.THRESH_BINARY);





         Bitmap roi = null;
         List<MatOfPoint> contours = new ArrayList<>();
         Mat hierachy = new Mat();
         Imgproc.findContours(mat_cny,contours,hierachy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

         for(int idx=0;idx>=0;idx=(int)hierachy.get(0,idx)[0])
         {
             MatOfPoint MOP = contours.get(idx);
             Rect rect = Imgproc.boundingRect(MOP);
             if(rect.width<30 || rect.height<30||rect.width<=rect.height|| rect.x<20||rect.y<20
                     || rect.width < rect.height*3 || rect.width >= rect.height*6) continue;

             roi = Bitmap.createBitmap(BitmapOpenCV,(int)rect.tl().x,(int)rect.tl().y,rect.width,rect.height);


         }
         image1 = Bitmap.createBitmap(img1.cols(),img1.rows(),Bitmap.Config.ARGB_8888);



         int[][] listarr = new int[8][4];

         List<MatOfPoint> contoursROI = new ArrayList<>();

         Mat hierarchyROI = new Mat();

         Mat matROI = new Mat();

         Mat ResultROI = new Mat();

         Utils.bitmapToMat(roi, matROI);

         Imgproc.cvtColor(matROI, ResultROI, Imgproc.COLOR_BGR2GRAY, 1);

         Imgproc.findContours(ResultROI, contoursROI, hierarchyROI, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

         int count = 0;

         for(int idx = 0; idx >= 0; idx = (int) hierarchyROI.get(0, idx)[0])
         {

             if(count>7) break;

             MatOfPoint matOfPoint = contoursROI.get(idx);

             Rect rect = Imgproc.boundingRect(matOfPoint);

             // 해상도별로 조절하기.

             if(rect.x < roi.getWidth()/20 || rect.x > roi.getWidth()-(roi.getWidth()/10) ||

                     rect.y < roi.getHeight()/10 || rect.y > roi.getHeight()-(roi.getHeight()/10) ||

                     rect.width < roi.getWidth()/50 || rect.width > roi.getWidth()/8 ||

                     rect.height <= roi.getHeight()/10 ) continue;



             Log.d("RECT : ", "x : " + rect.x + ", y : " + rect.y + ", w :" + rect.width + ", h : " + rect.height);

             Imgproc.rectangle(matROI, rect.tl(), rect.br(), new Scalar(255, 0, 0, 255), 1);



             listarr[count][0] = rect.x;

             listarr[count][1] = rect.y;

             listarr[count][2] = rect.width;

             listarr[count][3] = rect.height;

             count++;

         }

         if(count == 7) {

             // 오름차순 정렬

             for(int i = 0; i < 7; i++) {

                 for(int j = i; j < 7; j++) {

                     if(listarr[i][0] > listarr[j][0]) {

                         // 스왑

                         int[] temp = new int[4];

                         temp[0] = listarr[i][0];

                         temp[1] = listarr[i][1];

                         temp[2] = listarr[i][2];

                         temp[3] = listarr[i][3];

                         listarr[i][0] = listarr[j][0];

                         listarr[i][1] = listarr[j][1];

                         listarr[i][2] = listarr[j][2];

                         listarr[i][3] = listarr[j][3];

                         listarr[j][0] = temp[0];

                         listarr[j][1] = temp[1];

                         listarr[j][2] = temp[2];

                         listarr[j][3] = temp[3];

                     }

                 }

             }



             for(int i = 0; i < 7; i++) {

                 Bitmap tempBitmap = Bitmap.createBitmap(roi, listarr[i][0], listarr[i][1], listarr[i][2], listarr[i][3]);

                 tempBitmap = Bitmap.createScaledBitmap(tempBitmap, 10, 15, true);

                 if(i == 0)

                     imageViewROI1.setImageBitmap(tempBitmap);

                 else if(i == 1)

                     imageViewROI2.setImageBitmap(tempBitmap);

                 else if(i == 2)

                     imageViewROI3.setImageBitmap(tempBitmap);

                 else if(i == 3)

                     imageViewROI4.setImageBitmap(tempBitmap);

                 else if(i == 4)

                     imageViewROI5.setImageBitmap(tempBitmap);

                 else if(i == 5)

                     imageViewROI6.setImageBitmap(tempBitmap);

                 else if(i == 6)

                     imageViewROI7.setImageBitmap(tempBitmap);



                 int num_count = 0;

                 double max = 0.0;

                 int num = 0;

                 if(i==2) { // 문자

                     for(int n = 0; n < 10; n++) {

                         for(int j = 0; j < 15; j++) {

                             for(int k = 0; k < 10; k++) {

                                 if(ones[n][j][k] == tempBitmap.getPixel(k, j))

                                     num_count++;

                             }

                         }

                         double result = (double)num_count/proones[n]*100;

                         if(result > 100) result = 100;

                         if(result > max) {

                             max = result;

                             num = n;

                         }

                         num_count=0;

                     }

                 }

                 else { // 숫자

                     for(int n = 0; n < 10; n++) {

                         for(int j = 0; j < 15; j++) {

                             for(int k = 0; k < 10; k++) {

                                 if(nums[n][j][k] == tempBitmap.getPixel(k, j))

                                     num_count++;

                             }

                         }

                         double result = (double)num_count/pronums[n]*100;

                         if(result > 100) result = 100;

                         if(result > max) {

                             max = result;

                             num = n;

                         }

                         num_count=0;

                     }

                 }



                 avg+=max;

                 String floatnum = String.format("%.2f", max);



                 if(i == 2) { // 한글 처리

                     // 중략



                 }



                 int a = 0, b = 0, c = 0;

                 for(int x=0; x<tempBitmap.getHeight(); x++) {

                     for (int y = 0; y < tempBitmap.getWidth(); y++) {

                         if (tempBitmap.getPixel(y, x) == -1) {

                             Log.d("arr", "ones[5][" + a + "][" + b + "] = " + tempBitmap.getPixel(y, x) + ";");

                             c++;

                         }

                         b++;

                     }

                     b = 0;

                     a++;

                 }

                 Log.d("count" , ""+c);

             }

        else { // 숫자 처리

                 textViewResult.append(i + "번째 분석 결과 : " + num + " / 정확도 : " + floatnum +"%\n");

                 result += Integer.toString(num);

             }

         }


else if(count == 8) {

        // 오름차순 정렬

        for(int i = 0; i < 8; i++) {

            for(int j = i; j < 8; j++) {

                if(listarr[i][0] > listarr[j][0]) {

                    // 스왑

                    int[] temp = new int[4];

                    temp[0] = listarr[i][0];

                    temp[1] = listarr[i][1];

                    temp[2] = listarr[i][2];

                    temp[3] = listarr[i][3];

                    listarr[i][0] = listarr[j][0];

                    listarr[i][1] = listarr[j][1];

                    listarr[i][2] = listarr[j][2];

                    listarr[i][3] = listarr[j][3];

                    listarr[j][0] = temp[0];

                    listarr[j][1] = temp[1];

                    listarr[j][2] = temp[2];

                    listarr[j][3] = temp[3];

                }

            }

        }

        // 한글인 경우

        if(Math.abs(listarr[2][0]-listarr[3][0]) < 20) {

            if(listarr[2][1] > listarr[3][1]) {

                // 스왑

                int[] temp = new int[4];

                temp[0] = listarr[2][0];

                temp[1] = listarr[2][1];

                temp[2] = listarr[2][2];

                temp[3] = listarr[2][3];

                listarr[2][0] = listarr[3][0];

                listarr[2][1] = listarr[3][1];

                listarr[2][2] = listarr[3][2];

                listarr[2][3] = listarr[3][3];

                listarr[3][0] = temp[0];

                listarr[3][1] = temp[1];

                listarr[3][2] = temp[2];

                listarr[3][3] = temp[3];

            }

        }



        for(int i = 0; i < 8; i++) {

            Bitmap tempBitmap = Bitmap.createBitmap(roi, listarr[i][0], listarr[i][1], listarr[i][2], listarr[i][3]);

            tempBitmap = Bitmap.createScaledBitmap(tempBitmap, 10, 15, true);

            if(i == 0)

                imageViewROI1.setImageBitmap(tempBitmap);

            else if(i == 1)

                imageViewROI2.setImageBitmap(tempBitmap);

            else if(i == 2)

                imageViewROI3.setImageBitmap(tempBitmap);

            else if(i == 3)

                imageViewROI4.setImageBitmap(tempBitmap);

            else if(i == 4)

                imageViewROI5.setImageBitmap(tempBitmap);

            else if(i == 5)

                imageViewROI6.setImageBitmap(tempBitmap);

            else if(i == 6)

                imageViewROI7.setImageBitmap(tempBitmap);

            else if(i == 7)

                imageViewROI8.setImageBitmap(tempBitmap);



            int num_count = 0;

            double max = 0.0;

            int num = 0;

            // 자음

            if(i==2) {

                for(int n = 0; n < 10; n++) {

                    for(int j = 0; j < 15; j++) {

                        for(int k = 0; k < 10; k++) {

                            if(cons[n][j][k] == tempBitmap.getPixel(k, j))

                                num_count++;

                        }

                    }

                    double result = (double)num_count/procons[n]*100;

                    if(result > 100) result = 100;

                    if(result > max) {

                        max = result;

                        num = n;

                    }

                    num_count=0;

                }

            }



            // 모음

            else if(i==3) {

                for(int n = 0; n < 4; n++) {

                    for(int j = 0; j < 15; j++) {

                        for(int k = 0; k < 10; k++) {

                            if(vocs[n][j][k] == tempBitmap.getPixel(k, j))

                                num_count++;

                        }

                    }

                    double result = (double)num_count/provocs[n]*100;

                    if(result > 100) result = 100;

                    if(result > max) {

                        max = result;

                        num = n;

                    }

                    num_count=0;

                }

            }



            // 숫자

            else {

                for(int n = 0; n < 10; n++) {

                    for(int j = 0; j < 15; j++) {

                        for(int k = 0; k < 10; k++) {

                            if(nums[n][j][k] == tempBitmap.getPixel(k, j))

                                num_count++;

                        }

                    }

                    double result = (double)num_count/pronums[n]*100;

                    if(result > 100) result = 100;

                    if(result > max) {

                        max = result;

                        num = n;

                    }

                    num_count=0;

                }

            }



            avg+=max;

            String floatnum = String.format("%.2f", max);



            // 자음

            if(i==2) {

                // 중략

            }



            // 모음

            else if(i==3) {

                // 중략

            }



            // 숫자

            else {

                textViewResult.append(i + "번째 분석 결과 : " + num + " / 정확도 : " + floatnum +"%\n");

                result += Integer.toString(num);

            }

        }

    }

else {

        Toast.makeText(context, "숫자 생성 실패", Toast.LENGTH_SHORT).show();

    }



Utils.matToBitmap(matROI, roi2);



    imageViewROI = (ImageView)findViewById(R.id.image_result_ROI);

imageViewROI.setImageBitmap(roi2);



    String resultnum = String.format("%.2f", avg/count);

textViewResult.append("\n\n분석 결과 : " + result + " / 정확도 : " + resultnum + "%");

}



    중간에 문자 비교 코드는 너무 길어서 중략 처리했습니다.

        다음은 미리 저장해둔 문자 템플릿입니다.



        nums = new int[10][15][10];

        pronums = new int[10];



        cons = new int[10][15][10];

        procons = new int[10];



        vocs = new int[4][15][10];

        provocs = new int[4];



        ones = new int[10][15][10];

        proones = new int[10];



// 픽셀 개수

        pronums[0] = 50;

        pronums[1] = 40;

        pronums[2] = 40;

        pronums[3] = 45;

        pronums[4] = 50;

        pronums[5] = 55;

        pronums[6] = 50;

        pronums[7] = 37;

        pronums[8] = 50;

        pronums[9] = 40;



// 0

        nums[0][0][3] = -1; nums[0][0][4] = -1; nums[0][0][5] = -1; nums[0][0][6] = -1;

        nums[0][1][2] = -1; nums[0][1][3] = -1; nums[0][1][6] = -1; nums[0][1][7] = -1;

        nums[0][2][0] = -1; nums[0][2][1] = -1; nums[0][2][8] = -1; nums[0][2][9] = -1;

        nums[0][3][0] = -1; nums[0][3][1] = -1; nums[0][3][8] = -1; nums[0][3][9] = -1;

        nums[0][4][0] = -1; nums[0][4][1] = -1; nums[0][4][8] = -1; nums[0][4][9] = -1;

        nums[0][5][0] = -1; nums[0][5][1] = -1; nums[0][5][8] = -1; nums[0][5][9] = -1;

        nums[0][6][0] = -1; nums[0][6][1] = -1; nums[0][6][8] = -1; nums[0][6][9] = -1;

        nums[0][7][0] = -1; nums[0][7][1] = -1; nums[0][7][8] = -1; nums[0][7][9] = -1;

        nums[0][8][0] = -1; nums[0][8][1] = -1; nums[0][8][8] = -1; nums[0][8][9] = -1;

        nums[0][9][0] = -1; nums[0][9][1] = -1; nums[0][9][8] = -1; nums[0][9][9] = -1;

        nums[0][10][0] = -1; nums[0][10][1] = -1; nums[0][10][8] = -1; nums[0][10][9] = -1;

        nums[0][11][0] = -1; nums[0][11][1] = -1; nums[0][11][8] = -1; nums[0][11][9] = -1;

        nums[0][12][0] = -1; nums[0][12][1] = -1; nums[0][12][8] = -1; nums[0][12][9] = -1;

        nums[0][13][2] = -1; nums[0][13][3] = -1; nums[0][13][6] = -1; nums[0][13][7] = -1;

        nums[0][14][3] = -1; nums[0][14][4] = -1; nums[0][14][5] = -1; nums[0][14][6] = -1;



// 1

        nums[1][0][7] = -1; nums[1][0][8] = -1;

        nums[1][1][6] = -1; nums[1][1][7] = -1; nums[1][1][8] = -1;

        nums[1][2][1] = -1; nums[1][2][2] = -1; nums[1][2][3] = -1; nums[1][2][4] = -1; nums[1][2][5] = -1; nums[1][2][6] = -1; nums[1][2][7] = -1; nums[1][2][8] = -1;

        nums[1][3][0] = -1; nums[1][3][1] = -1; nums[1][3][2] = -1; nums[1][3][3] = -1; nums[1][3][4] = -1; nums[1][3][5] = -1; nums[1][3][6] = -1; nums[1][3][7] = -1; nums[1][3][8] = -1;

        nums[1][4][6] = -1; nums[1][4][7] = -1; nums[1][4][8] = -1;

        nums[1][5][7] = -1; nums[1][5][8] = -1;

        nums[1][6][7] = -1; nums[1][6][8] = -1;

        nums[1][7][7] = -1; nums[1][7][8] = -1;

        nums[1][8][7] = -1; nums[1][8][8] = -1;

        nums[1][9][7] = -1; nums[1][9][8] = -1;

        nums[1][10][7] = -1; nums[1][10][8] = -1;

        nums[1][11][7] = -1; nums[1][11][8] = -1;

        nums[1][12][7] = -1; nums[1][12][8] = -1;

        nums[1][13][7] = -1; nums[1][13][8] = -1;

        nums[1][14][7] = -1; nums[1][14][8] = -1;



// 2

        nums[2][0][3] = -1; nums[2][0][4] = -1;

        nums[2][1][1] = -1; nums[2][1][2] = -1; nums[2][1][5] = -1; nums[2][1][6] = -1;

        nums[2][2][0] = -1; nums[2][2][1] = -1; nums[2][2][7] = -1;

        nums[2][3][0] = -1; nums[2][3][1] = -1; nums[2][3][7] = -1; nums[2][3][8] = -1;

        nums[2][4][7] = -1; nums[2][4][8] = -1;

        nums[2][5][7] = -1; nums[2][5][8] = -1;

        nums[2][6][6] = -1; nums[2][6][7] = -1;

        nums[2][7][6] = -1; nums[2][7][7] = -1;

        nums[2][8][5] = -1; nums[2][8][6] = -1;

        nums[2][9][4] = -1; nums[2][9][5] = -1;

        nums[2][10][3] = -1; nums[2][10][4] = -1;

        nums[2][11][3] = -1; nums[2][11][4] = -1;

        nums[2][12][2] = -1; nums[2][12][3] = -1;

        nums[2][13][1] = -1; nums[2][13][2] = -1; nums[2][13][3] = -1;

        nums[2][14][1] = -1; nums[2][14][2] = -1; nums[2][14][3] = -1; nums[2][14][4] = -1; nums[2][14][5] = -1; nums[2][14][6] = -1; nums[2][14][7] = -1; nums[2][14][8] = -1; nums[2][14][9] = -1;



// 3

        nums[3][0][2] = -1; nums[3][0][3] = -1; nums[3][0][4] = -1; nums[3][0][5] = -1; nums[3][0][6] = -1; nums[3][0][7] = -1; nums[3][0][8] = -1; nums[3][0][9] = -1;

        nums[3][1][3] = -1; nums[3][1][4] = -1; nums[3][1][5] = -1; nums[3][1][6] = -1; nums[3][1][7] = -1; nums[3][1][8] = -1; nums[3][1][9] = -1;

        nums[3][2][7] = -1; nums[3][2][8] = -1;

        nums[3][3][6] = -1; nums[3][3][7] = -1;

        nums[3][4][5] = -1; nums[3][4][6] = -1;

        nums[3][5][4] = -1; nums[3][5][5] = -1; nums[3][5][6] = -1;

        nums[3][6][5] = -1; nums[3][6][6] = -1; nums[3][6][7] = -1;

        nums[3][7][7] = -1;

        nums[3][8][7] = -1; nums[3][8][8] = -1;

        nums[3][9][7] = -1; nums[3][9][8] = -1;

        nums[3][10][0] = -1; nums[3][10][1] = -1; nums[3][10][7] = -1;

        nums[3][11][0] = -1; nums[3][11][1] = -1; nums[3][11][7] = -1;

        nums[3][12][0] = -1; nums[3][12][1] = -1; nums[3][12][6] = -1;

        nums[3][13][1] = -1; nums[3][13][2] = -1; nums[3][13][3] = -1; nums[3][13][4] = -1; nums[3][13][5] = -1; nums[3][13][6] = -1;

        nums[3][14][2] = -1; nums[3][14][3] = -1; nums[3][14][4] = -1;



// 4

        nums[4][0][6] = -1; nums[4][0][7] = -1;

        nums[4][1][6] = -1; nums[4][1][7] = -1;

        nums[4][2][5] = -1; nums[4][2][6] = -1; nums[4][2][7] = -1;

        nums[4][3][4] = -1; nums[4][3][5] = -1; nums[4][3][6] = -1; nums[4][3][7] = -1;

        nums[4][4][3] = -1; nums[4][4][4] = -1; nums[4][4][6] = -1; nums[4][4][7] = -1;

        nums[4][5][3] = -1; nums[4][5][6] = -1; nums[4][5][7] = -1;

        nums[4][6][2] = -1; nums[4][6][3] = -1; nums[4][6][6] = -1; nums[4][6][7] = -1;

        nums[4][7][2] = -1; nums[4][7][6] = -1; nums[4][7][7] = -1;

        nums[4][8][1] = -1; nums[4][8][2] = -1; nums[4][8][6] = -1; nums[4][8][7] = -1;

        nums[4][9][1] = -1; nums[4][9][6] = -1; nums[4][9][7] = -1;

        nums[4][10][0] = -1; nums[4][10][1] = -1; nums[4][10][2] = -1; nums[4][10][3] = -1; nums[4][10][4] = -1; nums[4][10][5] = -1; nums[4][10][6] = -1; nums[4][10][7] = -1; nums[4][10][8] = -1; nums[4][10][9] = -1;

        nums[4][11][0] = -1; nums[4][11][1] = -1; nums[4][11][2] = -1; nums[4][11][3] = -1; nums[4][11][4] = -1; nums[4][11][5] = -1; nums[4][11][6] = -1; nums[4][11][7] = -1; nums[4][11][8] = -1; nums[4][11][9] = -1;

        nums[4][12][6] = -1; nums[4][12][7] = -1;

        nums[4][13][6] = -1; nums[4][13][7] = -1;

        nums[4][14][6] = -1; nums[4][14][7] = -1;



// 5

        nums[5][0][2] = -1; nums[5][0][3] = -1; nums[5][0][4] = -1; nums[5][0][5] = -1; nums[5][0][6] = -1; nums[5][0][7] = -1; nums[5][0][8] = -1;

        nums[5][1][1] = -1;  nums[5][1][2] = -1; nums[5][1][3] = -1; nums[5][1][4] = -1; nums[5][1][5] = -1; nums[5][1][6] = -1; nums[5][1][7] = -1;

        nums[5][2][1] = -1; nums[5][2][2] = -1;

        nums[5][3][1] = -1; nums[5][3][2] = -1;

        nums[5][4][1] = -1; nums[5][4][2] = -1;

        nums[5][5][1] = -1; nums[5][5][2] = -1;

        nums[5][6][1] = -1; nums[5][6][2] = -1; nums[5][6][3] = -1; nums[5][6][4] = -1; nums[5][6][5] = -1; nums[5][6][6] = -1; nums[5][6][7] = -1;

        nums[5][7][1] = -1; nums[5][7][2] = -1; nums[5][7][3] = -1; nums[5][7][4] = -1; nums[5][7][5] = -1; nums[5][7][6] = -1; nums[5][7][7] = -1; nums[5][7][8] = -1;

        nums[5][8][8] = -1; nums[5][8][9] = -1;

        nums[5][9][8] = -1; nums[5][9][9] = -1;

        nums[5][10][8] = -1; nums[5][10][9] = -1;

        nums[5][11][0] = -1; nums[5][11][1] = -1; nums[5][11][8] = -1; nums[5][11][9] = -1;

        nums[5][12][0] = -1; nums[5][12][1] = -1; nums[5][12][7] = -1; nums[5][12][8] = -1;

        nums[5][13][1] = -1; nums[5][13][2] = -1; nums[5][13][3] = -1; nums[5][13][4] = -1; nums[5][13][5] = -1; nums[5][13][6] = -1; nums[5][13][7] = -1;

        nums[5][14][2] = -1; nums[5][14][3] = -1; nums[5][14][4] = -1; nums[5][14][5] = -1; nums[5][14][6] = -1;



// 6

        nums[6][0][6] = -1; nums[6][0][7] = -1;

        nums[6][1][6] = -1;

        nums[6][2][5] = -1; nums[6][2][6] = -1;

        nums[6][3][4] = -1; nums[6][3][5] = -1;

        nums[6][4][4] = -1;

        nums[6][5][3] = -1; nums[6][5][4] = -1;

        nums[6][6][2] = -1; nums[6][6][3] = -1; nums[6][6][4] = -1; nums[6][6][5] = -1; nums[6][6][6] = -1;

        nums[6][7][2] = -1; nums[6][7][3] = -1; nums[6][7][4] = -1; nums[6][7][5] = -1; nums[6][7][6] = -1; nums[6][7][7] = -1; nums[6][7][8] = -1;

        nums[6][8][1] = -1; nums[6][8][2] = -1; nums[6][8][7] = -1; nums[6][8][8] = -1;

        nums[6][9][0] = -1; nums[6][9][1] = -1; nums[6][9][8] = -1; nums[6][9][9] = -1;

        nums[6][10][0] = -1; nums[6][10][1] = -1; nums[6][10][8] = -1; nums[6][10][9] = -1;

        nums[6][11][0] = -1; nums[6][11][1] = -1; nums[6][11][8] = -1; nums[6][11][9] = -1;

        nums[6][12][0] = -1; nums[6][12][1] = -1; nums[6][12][2] = -1; nums[6][12][7] = -1; nums[6][12][8] = -1;

        nums[6][13][1] = -1; nums[6][13][2] = -1; nums[6][13][3] = -1; nums[6][13][4] = -1; nums[6][13][5] = -1; nums[6][13][6] = -1; nums[6][13][7] = -1;

        nums[6][14][3] = -1; nums[6][14][4] = -1; nums[6][14][5] = -1;



// 7

        nums[7][0][0] = -1; nums[7][0][1] = -1; nums[7][0][2] = -1; nums[7][0][3] = -1; nums[7][0][4] = -1; nums[7][0][5] = -1; nums[7][0][6] = -1; nums[7][0][7] = -1; nums[7][0][8] = -1; nums[7][0][9] = -1;

        nums[7][1][0] = -1; nums[7][1][1] = -1; nums[7][1][2] = -1; nums[7][1][6] = -1; nums[7][1][7] = -1; nums[7][1][8] = -1; nums[7][1][9] = -1;

        nums[7][2][0] = -1; nums[7][2][1] = -1; nums[7][2][7] = -1; nums[7][2][8] = -1;

        nums[7][3][7] = -1;

        nums[7][4][7] = -1;

        nums[7][5][6] = -1;

        nums[7][6][5] = -1; nums[7][6][6] = -1;

        nums[7][7][5] = -1;

        nums[7][8][5] = -1;

        nums[7][9][4] = -1; nums[7][9][5] = -1;

        nums[7][10][4] = -1;

        nums[7][11][3] = -1; nums[7][11][4] = -1;

        nums[7][12][3] = -1;

        nums[7][13][2] = -1; nums[7][13][3] = -1;

        nums[7][14][2] = -1;



// 8

        nums[8][0][4] = -1; nums[8][0][5] = -1;

        nums[8][1][2] = -1; nums[8][1][3] = -1; nums[8][1][4] = -1; nums[8][1][5] = -1; nums[8][1][6] = -1; nums[8][1][7] = -1;

        nums[8][2][1] = -1; nums[8][2][2] = -1; nums[8][2][7] = -1; nums[8][2][8] = -1;

        nums[8][3][1] = -1; nums[8][3][8] = -1;

        nums[8][4][0] = -1; nums[8][4][1] = -1; nums[8][4][8] = -1;

        nums[8][5][1] = -1; nums[8][5][8] = -1;

        nums[8][6][2] = -1; nums[8][6][3] = -1; nums[8][6][4] = -1; nums[8][6][5] = -1; nums[8][6][6] = -1; nums[8][6][7] = -1;

        nums[8][7][2] = -1; nums[8][7][3] = -1; nums[8][7][4] = -1; nums[8][7][5] = -1; nums[8][7][6] = -1; nums[8][7][7] = -1;

        nums[8][8][1] = -1; nums[8][8][2] = -1; nums[8][8][7] = -1; nums[8][8][8] = -1;

        nums[8][9][0] = -1; nums[8][9][1] = -1; nums[8][9][8] = -1; nums[8][9][9] = -1;

        nums[8][10][0] = -1; nums[8][10][1] = -1; nums[8][10][8] = -1; nums[8][10][9] = -1;

        nums[8][11][0] = -1; nums[8][11][1] = -1; nums[8][11][8] = -1; nums[8][11][9] = -1;

        nums[8][12][1] = -1; nums[8][12][2] = -1; nums[8][12][7] = -1; nums[8][12][8] = -1;

        nums[8][13][1] = -1; nums[8][13][2] = -1; nums[8][13][3] = -1; nums[8][13][6] = -1; nums[8][13][7] = -1;

        nums[8][14][3] = -1; nums[8][14][4] = -1; nums[8][14][5] = -1; nums[8][14][6] = -1;



// 9

        nums[9][0][3] = -1; nums[9][0][4] = -1; nums[9][0][5] = -1;

        nums[9][1][1] = -1; nums[9][1][2] = -1; nums[9][1][3] = -1; nums[9][1][4] = -1; nums[9][1][5] = -1; nums[9][1][6] = -1; nums[9][1][7] = -1;

        nums[9][2][0] = -1; nums[9][2][1] = -1; nums[9][2][7] = -1; nums[9][2][8] = -1;

        nums[9][3][0] = -1; nums[9][3][1] = -1; nums[9][3][7] = -1; nums[9][3][8] = -1;

        nums[9][4][0] = -1; nums[9][4][1] = -1; nums[9][4][7] = -1; nums[9][4][8] = -1;

        nums[9][5][1] = -1; nums[9][5][2] = -1; nums[9][5][8] = -1; nums[9][5][9] = -1;

        nums[9][6][1] = -1; nums[9][6][2] = -1; nums[9][6][3] = -1; nums[9][6][8] = -1; nums[9][6][9] = -1;

        nums[9][7][3] = -1; nums[9][7][4] = -1; nums[9][7][5] = -1; nums[9][7][6] = -1; nums[9][7][7] = -1; nums[9][7][8] = -1;

        nums[9][8][5] = -1; nums[9][8][6] = -1; nums[9][8][7] = -1;

        nums[9][9][6] = -1; nums[9][9][7] = -1;

        nums[9][10][6] = -1;

        nums[9][11][6] = -1; nums[9][11][7] = -1;

        nums[9][12][5] = -1;

        nums[9][13][4] = -1; nums[9][13][5] = -1;



// 자음 및 모음 중략...

*/

     }
}


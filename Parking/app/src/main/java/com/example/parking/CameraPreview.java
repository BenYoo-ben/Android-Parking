package com.example.parking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


// Get video data from Camera = CameraPreview
class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {

    private final String TAG = "CameraPreview";

    private int mCameraID;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;
    private List<Size> mSupportedPreviewSizes;
    private Size mPreviewSize;
    private boolean isPreview = false;

    private AppCompatActivity mActivity;
    private String resultString = "";
   private MLKit mlk;
    protected void getMLK(MLKit mlk)
    {
        this.mlk = mlk;
    }

    private int focusing=0;

    private int scaledX=500, scaledY=500;
    private long imagecode;

    private Context mContext;

    private ImageView IV;
    private ImageView CI;

    public void getmContext(Context c){this.mContext=c;}
    public void getImageView(ImageView iv)
    {
        this.IV = iv;
    }
    int ThreadCount =0;

    public CameraPreview(Context context, AppCompatActivity activity, int cameraID, SurfaceView surfaceView, ImageView CI) {
        super(context);


        Log.d("@@@", "Preview");

        this.CI = CI;

        mActivity = activity;
        mCameraID = cameraID;
        mSurfaceView = surfaceView;


        mSurfaceView.setVisibility(View.VISIBLE);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }




    public void surfaceCreated(SurfaceHolder holder) {


        try {
            //카메라 열기
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            //카메라가 사용중이거나 사용불가능
            Log.e(TAG, "Camera " + mCameraID + " is not available: " + e.getMessage());
        }


        //카메라 정보 받아오기
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;
        mDisplayOrientation = mActivity.getWindowManager().getDefaultDisplay().getRotation();

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);



        mSupportedPreviewSizes =  mCamera.getParameters().getSupportedPreviewSizes();
        requestLayout();

        //카메라 사양 읽기 + 설정
        Camera.Parameters params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {

            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

            mCamera.setParameters(params);
        }


        try {

            mCamera.setPreviewDisplay(holder);

            mCamera.startPreview();
            isPreview = true;
            Log.d(TAG, "Camera preview started.");





        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }


//surface가 종료될때
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            if (isPreview)
                mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isPreview = false;
        }

    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // 프리뷰 사이즈 맞추기
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        //적정 사이즈를 찾지 못할 경우
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // 화면이 돌아가거나 할 경우

        if (mHolder.getSurface() == null) {
            // 프리뷰 존재 X
            Log.d(TAG, "Preview surface does not exist");
            return;
        }



        try {
            mCamera.stopPreview();
            Log.d(TAG, "Preview stopped.");
        } catch (Exception e) {

            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
           mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }



//카메라 회전
    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    public void Focused() {
        if (focusing == 0) {
            focusing=1;
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    focusing=0;
                }
            });
        }
    }
    public void takePicture(){


        mCamera.autoFocus (new Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {

                Log.d("TakePic","Focusing...");
                if(success){

                    Log.d("TakePic","Success!");
                TakePic tp = new TakePic(mCamera);
                tp.start();

                }
                else
                {
                    Log.d("TakePic","Failure");
                }

            }

        });
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {

        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };






        //사진 촬영 및 이미지 프로세싱 + 저장할 쓰레드
    class TakePic extends Thread implements Runnable
    {

        private Camera mCamera;
        private Activity CM;

        TakePic(Camera mCamera)
        {
            this.mCamera = mCamera;
            Log.d("TakePic","ThreadBorn"+ThreadCount);
            ThreadCount++;
        }


        @Override
        public void destroy() {
            super.destroy();
            Log.d("TakePic","ThreadDying"+ThreadCount);
            ThreadCount--;
        }

        //카메라 콜백 메소드
        Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] data, final Camera camera) {
                    //프로세스 이미지 Asynctask
                new ProcessImageTask(data).execute();


            }
        };
        @Override
        public void run()
        {
            //Loading GIF 보이게 하기
            mActivity.runOnUiThread(new Runnable()
            {

                @Override
                public void run() {
                    Log.d("TakePic","Setting Loading IV..");
                    IV.setVisibility(View.GONE);
                    CI.setVisibility(View.VISIBLE);
                }
            }
            );


            this.mCamera.takePicture(null, null, this.jpegCallback);


            Log.d("TakePic","After TakePhoto");

            try {
                this.sleep(1000);
                mCamera.stopPreview();
                mCamera.startPreview();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        };
            //사진 저장 AsyncTask
        private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

            @Override
            protected Void doInBackground(byte[]... data) {
                FileOutputStream outStream = null;


                try {

                    File path = new File (mContext.getFilesDir().getAbsolutePath());
                    if (!path.exists()) {
                        path.mkdirs();
                    }
                    Log.d("PATH",path.toString());

                    String fileName =imagecode+".jpg";
                    File outputFile = new File(path, fileName);
                    File outputText = new File(path,"ResultText.txt");


                    outStream = new FileOutputStream(outputFile);
                    outStream.write(data[0]);
                    outStream.flush();

                    outStream = new FileOutputStream(outputText);
                    if(resultString!=null) {
                        byte[] tmpdata = resultString.getBytes();
                        outStream.write(tmpdata);
                    }
                    outStream.close();

                    Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
                            + outputFile.getAbsolutePath());

                    // Add to gallery
                    Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(Uri.fromFile(outputFile));
                    getContext().sendBroadcast(mediaScanIntent);


                    try {
                        mCamera.setPreviewDisplay(mHolder);
                        mCamera.startPreview();
                        Log.d(TAG, "Camera preview started.");
                    } catch (Exception e) {
                        Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                    }

                    //imagecoe to mlkit
                    mlk.setImageCode(imagecode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }

        private class ProcessImageTask extends AsyncTask<byte[], Void, Void> {

            byte[] data;
            byte[] currentData;

            ProcessImageTask(final byte[] data){
                this.data = data;
            }
            @Override
            protected Void doInBackground(byte[]... datab) {
                Log.d("TakePic","HERE?");
                //이미지의 너비와 높이 결정

                int w = mCamera.getParameters().getPictureSize().width;
                int h = mCamera.getParameters().getPictureSize().height;
                int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);


                //byte array를 bitmap으로 변환
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);


                //이미지를 디바이스 방향으로 회전
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
                Calendar c = Calendar.getInstance();

                imagecode = c.getTimeInMillis();


                //MLKit 으로 글자인식
                MLKit mlkOnThread = new MLKit(mlk,mContext);
                mlkOnThread.setImageCode(c.getTimeInMillis());
                mlkOnThread.runTextRecognition(bitmap);


                // mlk.runTextRecognition(bitmap);



                //용량확보를 위해 사진 사이즈 줄이기
                final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,scaledX, scaledY, true);
                //bitmap을 byte array로 변환
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                currentData = stream.toByteArray();

                //파일로 저장




                //로딩 GIF 치우고 촬영된 사진으로 대체
                mActivity.runOnUiThread(new Runnable()
                {

                    @Override
                    public void run() {
                        IV.setVisibility(View.VISIBLE);
                        IV.setImageBitmap(scaledBitmap);
                        CI.setVisibility(View.GONE);
                    }
                });


                Log.d("TakePic","ThreadInterrupt"+ThreadCount);
                interrupt();

                return null;
            }


            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);


                new SaveImageTask().execute(currentData);
                Log.d("TakePic","ImageSaveStart");
            }




        }
    }

}
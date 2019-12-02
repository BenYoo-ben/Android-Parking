package com.example.parking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;


public class CameraMain extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "android_camera_example";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK; // Camera.CameraInfo.CAMERA_FACING_FRONT

    private SurfaceView surfaceView;
    private CameraPreview mCameraPreview;
    private View mLayout;
    private Button Confirm_Button;
    private MLKit mlk;
    private EditText[] VPC = new EditText[3];
    private ImageView IV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No state bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Keep screen on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.camera_layout);

        mLayout = findViewById(R.id.layout_main);
        surfaceView = findViewById(R.id.surfaceView);


        //Until permission is not granted, surfaceview is not visible.
        surfaceView.setVisibility(View.GONE);
        VPC[0] = (EditText)findViewById(R.id.edit_v);
        VPC[1] = (EditText)findViewById(R.id.edit_p);
        VPC[2] = (EditText)findViewById(R.id.edit_c);
        IV = (ImageView)findViewById(R.id.ImagePreview);

        Confirm_Button = (Button)findViewById(R.id.Confirm_Button);
        Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    boolean b = true;
                    for(int i=0;i<3;i++)
                    {
                        if(VPC[0].getText().toString().equals("")){
                            b=false;
                        }
                    }
                    if(b==true)
                    {
                        Calendar c = Calendar.getInstance();

                        Vehicle newV = new Vehicle(Settings.location,VPC[2].getText().toString(),VPC[1].getText().toString(),VPC[0].getText().toString(),
                            new Timer().SDF.format(c.getTime()),mlk.getImageCode());
                        FirebaseController.addVehicle(newV);
                        finish();
                    }
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraPreview.takePicture();
            }
        });
        surfaceView.setOnTouchListener(new View.OnTouchListener()
        {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mCameraPreview.Focused();
                }
                return false;
            }
        });




        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


            if ( cameraPermission == PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                startCamera();


            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "This application needs external storage permission",
                            Snackbar.LENGTH_INDEFINITE).setAction("Confirm", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions( CameraMain.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();


                } else {
                    //If user didn't reject permission then request the permission right away.
                    // request record are stored at onRequestPermissionResult
                    ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

        } else {

            final Snackbar snackbar = Snackbar.make(mLayout, "This device doesn't support Camera",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Confirm", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }


    }



    void startCamera(){

        // Create the Preview view and set it as the content of this Activity.
        mlk = new MLKit(getApplicationContext());
        mlk.getVPC(VPC);
        mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, surfaceView);
        mCameraPreview.setBackgroundColor(Color.TRANSPARENT);
        mCameraPreview.getMLK(mlk);
        mCameraPreview.getImageView(IV);
        mCameraPreview.getmContext(getApplicationContext());

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {

                startCamera();
            }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "Permission rejected. Please re-run this app and confirm. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("Confirm", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {

                    Snackbar.make(mLayout, "Need Permission ",
                            Snackbar.LENGTH_INDEFINITE).setAction("Confirm", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }


    }


}

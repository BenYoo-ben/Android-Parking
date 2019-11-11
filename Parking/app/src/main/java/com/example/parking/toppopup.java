package com.example.parking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class toppopup extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_popup);
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int w = dm.widthPixels;
    int h = dm.heightPixels;
        getWindow().setLayout((int)w,(int)(h*0.2));
      WindowManager.LayoutParams params = getWindow().getAttributes();
      params.gravity = Gravity.TOP;
      params.x=0;
      params.y=-20;
      getWindow().setAttributes(params);
    }

}

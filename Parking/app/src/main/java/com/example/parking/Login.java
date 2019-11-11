package com.example.parking;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener{

    float initX,diffX, pushSensitivity = 150;

    float initY,diffY;

    LayoutInflater LI;
    FrameLayout framelayout;
    PopupWindow popup;

    private void showToast(String message)
    {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseController.onStarter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        LI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popup = new PopupWindow(LI.inflate(R.layout.top_popup,null,false), ActionBar.LayoutParams.MATCH_PARENT,150,true);
        popup.setAnimationStyle(R.anim.fade_in);
        framelayout = new FrameLayout(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m)
    {
        if(m.getAction() == MotionEvent.ACTION_DOWN) {
            initX = m.getRawX();
            initY = m.getRawY();
            LI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            popup = new PopupWindow(LI.inflate(R.layout.top_popup,null,false), ActionBar.LayoutParams.MATCH_PARENT,150,true);
            popup.setAnimationStyle(R.anim.fade_out);
        }
        if(m.getAction()==MotionEvent.ACTION_UP)
        {
            diffY = initY - m.getRawY();
            diffX = initX - m.getRawX();
            if(diffY < -pushSensitivity)
            {
                showToast("WORKED?");
                popup.showAtLocation(framelayout, Gravity.TOP,0,0);

                return super.onTouchEvent(m);
            }
            else if(diffX > pushSensitivity)
            {
                //NEXT
                startActivity(new Intent(this,ActivityA.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                return super.onTouchEvent(m);
            }
            else if(diffX < -(pushSensitivity*2))
            {
                //Previous
                startActivity(new Intent(this,ActivityC.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                return super.onTouchEvent(m);
            }

        }
        return super.onTouchEvent(m);
    }




    @Override
    public void onClick(View view) {

    }

    public void intentA(View view) {
        startActivity(new Intent(this,ActivityA.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


}

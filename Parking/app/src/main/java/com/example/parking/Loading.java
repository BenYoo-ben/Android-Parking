package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {
    Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        //이미지 서서히 밝아지는 효과를 위한 쓰레드
        ThemeModifier tm = new ThemeModifier((ImageView) findViewById(R.id.loading_imageview));
        tm.start();

       runnable = new Runnable() {

            @Override
            public void run() {
                //초기 실행인지 확인.
                if(FirebaseController.very_first_run==0)
                {
                    StartA();
                }
                else
                {
                    StartSettingsScreen();
                }

            }
        };



    }

    void StartA() {
        startActivity(new Intent(this, ActivityA.class));
        this.finish();
    }
    void StartSettingsScreen()
    {
        startActivity(new Intent(this,SettingsScreen.class));
        this.finish();
    }




    //이미지 서서히 밝아지는 효과를 위한 쓰레드
    class ThemeModifier extends Thread implements Runnable
    {
        ImageView iv;
        float f=0.0f;


        ThemeModifier(ImageView iv)
        {
            this.iv=iv;
        }
        @Override
        public void run()
        {
            FirebaseController.onStarter();

            while(true)
            {


                try {
                    iv.setAlpha(f);
                    f+=0.005f;


                    this.sleep(10);

                //loading_state_value : 현재주차장상황 + 과거수익기록 + 개인설정 모두 로드되었을때 1이다.
                    if(FirebaseController.loading_state_value >=0.99999)
                    {
                        runOnUiThread(runnable);

                        interrupt();
                        break;
                    }
                    if(FirebaseController.very_first_run==1)
                    {
                        runOnUiThread(runnable);
                        interrupt();
                        break;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }


    }


}


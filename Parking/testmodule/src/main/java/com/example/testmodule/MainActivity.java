package com.example.testmodule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmodule.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initEvent();

    }

    private void initEvent() {
        findViewById(R.id.Send_msg_Btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("정보");
        builder.setMessage("name:1111/n number:11111");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i){

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
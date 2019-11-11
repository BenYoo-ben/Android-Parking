
package com.example.testmodule;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements
        View.OnClickListener {
    Button send;
    EditText phone_Number, messgage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button) findViewById(R.id.Send_msg_Btn);
        phone_Number = (EditText) findViewById(R.id.EditText_PhoneNumber)
        ;
        messgage = (EditText) findViewById(R.id.MainActivity_Message);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone_Num = phone_Number.getText().toString();
        String send_msg = messgage.getText().toString();
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone_Num, null, send_msg,
                    null, null);
        } catch (Exception e) {
            Toast.makeText(this, "Sms not Sent", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

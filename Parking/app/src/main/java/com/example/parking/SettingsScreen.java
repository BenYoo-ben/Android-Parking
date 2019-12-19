package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;


public class SettingsScreen extends AppCompatActivity {



    static int hourlyfair ;
    static String contact;

    //static String imgloc =
    static String location;
    static int autosmson;

    Switch AutoSwitch;
    EditText[] ed = new EditText[3];

    public int getHourlyfair(){return hourlyfair;}
    public String getContact(){return contact;}
    public String getLocation(){return location;}
    public int getAutosmson(){return autosmson;}

    public void setHourlyfair(int hourlyfair){this.hourlyfair = hourlyfair;}
    public void setContact(String contact){this.contact = contact;}
    public void setLocation(String location){ this.location =location; }
    public void setAutosmson(int autosmson){this.autosmson = autosmson;}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ed[0] = (EditText)findViewById(R.id.FeeED);
        ed[1] = (EditText)findViewById(R.id.ContactED);
        ed[2] = (EditText)findViewById(R.id.LocationED);
        AutoSwitch = (Switch)findViewById(R.id.Switch);


        if(FirebaseController.very_first_run!=1)
        {
            ed[0].setText(String.valueOf(getHourlyfair()));
            ed[1].setText(getContact());
            ed[2].setText(getLocation());
            if(autosmson==0)
            {
                AutoSwitch.setChecked(true);
            }



        }

    }

    public static void obtainSettings(Settings s)
    {
       hourlyfair = s.getHourlyfair();
       contact = s.getContact();
       location = s.getLocation();
       autosmson = s.getAutosmson();

    }

    public void Settings_Confirm(View view)
    {
        EditText[] ed = new EditText[3];
        ed[0] = (EditText)findViewById(R.id.FeeED);
        ed[1] = (EditText)findViewById(R.id.ContactED);
        ed[2] = (EditText)findViewById(R.id.LocationED);
        if(ed[0].getText().toString().length()!=0 &&ed[1].getText().toString().length()!=0 && ed[2].getText().toString().length()!=0 )
        {
            Toast.makeText(this,"Settings Saved.",Toast.LENGTH_LONG).show();
            setHourlyfair(Integer.parseInt(ed[0].getText().toString()));
            setContact(ed[1].getText().toString());
            setLocation(ed[2].getText().toString());
            if(AutoSwitch.isChecked())
            setAutosmson(0);
            else
                setAutosmson(1);
            FirebaseController.updateSettings();
            FirebaseController.very_first_run=0;
            toFirst(null);
            finish();
        }
        else
        {
            Toast.makeText(this,"항목을 모두 적어주세요",Toast.LENGTH_LONG).show();
        }

    }
    public void toFirst(View view)
    {
        if(FirebaseController.very_first_run!=1)
        {
            startActivity(new Intent(this,ActivityA.class));
            Animatoo.animateZoom(this);
        }

    }

    public void toSecond(View view)
    {
        if(FirebaseController.very_first_run!=1)
        {
            startActivity(new Intent(this,ActivityB.class));
            Animatoo.animateZoom(this);
        }

    }

}

package com.example.parking;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Iterator;


public class ActivityA extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    DisplayMetrics displayMetrics;


    Vehicle[] veh;
    LinearLayout MiddleLayout;
    TextView hidden_name, hidden_phone, hidden_location, hidden_time, timeView, dateView;
    ImageView iv1, iv2, iv3, iv4;
    ImageButton SearchButton;
    EditText SearchEditText;

    LinearLayout ImageLayout1, TextLayout1;
    LinearLayout ImageLayout2, TextLayout2;
    LinearLayout ImageLayout;
    LinearLayout TextLayout;



    float initX, diffX, pushSensitivity = 150;
    float initY, diffY;

    LayoutInflater LI;
    FrameLayout framelayout;
    PopupWindow popup;


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    Vehicle LastSelectedVehicle = new Vehicle();



    Vehicle addContent(LinearLayout ImageLayout, LinearLayout TextLayout, Vehicle veh) {
        LinearLayout imageLayout;
        LinearLayout textLayout;
        imageLayout = ImageLayout;
        textLayout = TextLayout;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_width = displayMetrics.widthPixels;

        ImageView imageview = new ImageView(this);
        imageview = new ImageView(getApplicationContext());




        imageview.setScaleType(ImageView.ScaleType.FIT_XY);

        LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams((int) (screen_width * 0.45), LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f);

        imageview.setImageBitmap(BitmapFactory.decodeFile(Settings.imgloc+veh.getImagecode()));
        Log.d("HELPME!!",Settings.imgloc+veh.getImagecode());;
        imageview.setPadding(2, 2, 2, 2);


        // ilp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        // ilp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        imageview.setLayoutParams(ilp);
        imageview.setOnClickListener(this);


        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clockdroid2_dial);
        imageview.setBackgroundResource(R.drawable.image_border);
        //iv.setImageBitmap(bitmap);


        imageview.requestLayout();
        imageLayout.addView(imageview);


        TextView textview = new TextView(getApplicationContext());
        textview.setText(veh.getVehicle_num());
        textview.setTextSize(18);
        Typeface tf = ResourcesCompat.getFont(this, R.font.kenteken);
        textview.setTypeface(tf);
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tlp.setMargins(0, 30, 0, 0);
        textview.setLayoutParams(tlp);

        textview.requestLayout();
        textLayout.addView(textview);

        textview.setBackgroundResource(R.drawable.image_border2);
        textview.setGravity(View.TEXT_ALIGNMENT_CENTER);
        veh.iv = imageview;
        veh.tv = textview;
        return veh;
    }


    void removeView(LinearLayout[] IVs, LinearLayout[] TVs, int index) {/*
        System.out.println("Requested Delete is "+index);

        if(removeOrder==0){
            IVs[index%2].removeViewAt(index/2);
            TVs[index%2].removeViewAt(index/2);
        removeOrder=1;
        }else
        {
            IVs[~(index%2)].removeViewAt(index/2);
            TVs[~(index%2)].removeViewAt(index/2);
            removeOrder=0;

        }*/


        /*ImageLayout1.removeAllViews();
        ImageLayout2.removeAllViews();
        TextLayout1.removeAllViews();;
        TextLayout2.removeAllViews();
        setCarView(IVs,TVs);
*/


    }


    void removeView(Vehicle v) {
        LinearLayout parentImageLayout = (LinearLayout) (v.iv.getParent());
        LinearLayout parentTextLayout = (LinearLayout) (v.tv.getParent());
        parentImageLayout.removeView(v.iv);
        parentTextLayout.removeView(v.tv);

    }

    void setCarView(LinearLayout imageLayout, LinearLayout textLayout) {

        Iterator i = FirebaseController.Vehicles.iterator();
        clearCarView(imageLayout,textLayout);
        System.out.println("CARVIEW START");
        while (i.hasNext()) {

            addContent(ImageLayout, TextLayout, (Vehicle) i.next());
        }
        System.out.println("END");
        return;
    }
    void clearCarView(LinearLayout imageLayout, LinearLayout textLayout)
    {
        imageLayout.removeAllViews();

        textLayout.removeAllViews();

    }


    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        setCarView(ImageLayout, TextLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        Resources resources = getApplicationContext().getResources();
        displayMetrics = new DisplayMetrics();
        displayMetrics = resources.getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        LI = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popup = new PopupWindow(LI.inflate(R.layout.top_popup, null, false), ActionBar.LayoutParams.MATCH_PARENT, 150, true);
        popup.setAnimationStyle(R.anim.blink);
        framelayout = new FrameLayout(this);

        timeView = (TextView) findViewById(R.id.timeview);
        dateView = (TextView) findViewById(R.id.dateview);
        MiddleLayout = (LinearLayout) findViewById(R.id.linear_middle);
        ImageLayout = (LinearLayout) findViewById(R.id.linear_image1);
        TextLayout = (LinearLayout) findViewById(R.id.linear_text1);


        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);

        hidden_location = (TextView) findViewById(R.id.location_textview);
        hidden_name = (TextView) findViewById(R.id.name_textview);
        hidden_phone = (TextView) findViewById(R.id.phone_textview);
        hidden_time = (TextView) findViewById(R.id.time_textview);

        SearchButton = (ImageButton) findViewById(R.id.search_button7);
        SearchEditText = (EditText) findViewById(R.id.search_edit_text7);

        SearchEditText.addTextChangedListener(this);

        TimeManager tm = new TimeManager(timeView, dateView);
        tm.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            initX = m.getRawX();
            initY = m.getRawY();
            if (initY < MiddleLayout.getY() || initY > MiddleLayout.getY() + (0.1) * displayMetrics.heightPixels) {
                MiddleLayout.setVisibility(View.GONE);
            }

        }
        if (m.getAction() == MotionEvent.ACTION_UP) {
            diffY = initY - m.getRawY();
            diffX = initX - m.getRawX();
            if (diffY < -pushSensitivity) {
                showToast("WORKED?");
                popup.showAtLocation(framelayout, Gravity.TOP, 0, 0);

                return super.onTouchEvent(m);
            } else if (diffX > pushSensitivity) {
                //NEXT

                startActivity(new Intent(this, ActivityB.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return super.onTouchEvent(m);
            } else if (diffX < -(pushSensitivity * 2)) {
                //Previous
                startActivity(new Intent(this, ActivityD.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return super.onTouchEvent(m);
            }

        }
        return super.onTouchEvent(m);
    }

    @Override
    protected void onStart() {
        setTheme(R.style.Launcher);
        super.onStart();

        setCarView(ImageLayout, TextLayout);

    }


    @Override
    public void onClick(View view) {

        Iterator<Vehicle> i = FirebaseController.Vehicles.iterator();
        int count = 0;
        while (i.hasNext()) {
            Vehicle veh = ((Vehicle) i.next());
            if (view == veh.iv) {

                VehicleSelect(veh);
                LastSelectedVehicle = veh;
                return;
            }
            count++;
        }


    }

    void VehicleSelect(Vehicle veh)
    {
        veh.iv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
        hidden_location.setText(veh.getLocation());
        hidden_name.setText(veh.getName());
        hidden_phone.setText(veh.getContact());
        hidden_time.setText(Integer.toString(veh.getMinutes()));
        MiddleLayout.setVisibility(View.VISIBLE);
    }






    public void ShowCarInfo(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(LastSelectedVehicle.getVehicle_num()+" - 정보");
        builder.setMessage("Name : "+LastSelectedVehicle.getName()+"\nPhone : "+LastSelectedVehicle.getContact()+"\nArrival : "+LastSelectedVehicle.getArrival_time()+"\nLocation : "+LastSelectedVehicle.getLocation()+"\nimaegcode : "+LastSelectedVehicle.getImagecode());

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i){

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void intentA(View view) {

        showToast("Current Screen");

    }

    public void addNewCar(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Option");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL: //Manual Input
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        startCamera();
                        break;

                }
            }
            void startCamera()
            {
                Intent a = new Intent(getApplicationContext(),CameraMain.class);
                startActivity(a);
            }
        };
        builder.setMessage("Manually / Camera");
        builder.setNeutralButton("Manual", dialogClickListener);
        builder.setPositiveButton("Camera", dialogClickListener);




        builder.show();

/*
        Vehicle newV = LastSelectedVehicle.randomcarGen();
                newV.setFair(Settings.hour_fair);

            FirebaseController.addVehicle(newV);
            addContent(ImageLayouts,TextLayouts,newV);
  */
    }
    protected void ManualCarIn()
    {/*
        final EditText et = new EditText(getApplicationContext());
        FrameLayout container = new FrameLayout(getApplicationContext());

        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        et.setLayoutParams(params);

        container.addView(et);

        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getApplicationContext(),R.style.MyAlertDialogStyle);

        alt_bld.setTitle("닉네임 변경").setMessage("변경할 닉네임을 입력하세요")
                .setIcon(R.drawable.check_dialog_64).setCancelable(false).setView(container)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        String value = et.getText().toString();

                        user_name.setText(value);

                    }

                });

        AlertDialog alert = alt_bld.create();

        alert.show();

    */}
    public void intentB(View view) {

        startActivity(new Intent(this,ActivityB.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }
    public void CarOut(View view)  {
        removeView(LastSelectedVehicle);
        FirebaseController.removeVehicleCurrent(LastSelectedVehicle);
        Income income = new Income(LastSelectedVehicle);

       FirebaseController.addIncome(income);


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void intentD(View view)  {

        //need Change
      PDFPrint pdfp = new PDFPrint("test_pdf.pdf",this,LastSelectedVehicle);

    }

    public Vehicle SearchVehicleWithNum(String query)
    {
        Iterator<Vehicle> i = FirebaseController.Vehicles.iterator();

       while(i.hasNext())
       {
           Vehicle veh =  (Vehicle)i.next();

           if(veh.getVehicle_num().equals(query))
           {
               VehicleSelect(veh);
               return veh;
           }
       }
       return null;

    }

    public void Search(View view)
    {
        SearchVehicleWithNum(SearchEditText.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() < 1 || start >= s.length() || start < 0)
            return;

        // If it was Enter
        if (s.subSequence(start, start + 1).toString().equalsIgnoreCase("\n")) {


            SearchVehicleWithNum(SearchEditText.getText().toString().substring(0,SearchEditText.getText().toString().length()-1));
            showToast(SearchEditText.getText().toString());
            SearchEditText.setText("");

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}




class TimeManager extends Thread implements Runnable
{
    TextView timeView,dateView;
    SimpleDateFormat format1;
    SimpleDateFormat format2;
    TimeManager(TextView time, TextView date)
    {

            timeView = time;
            dateView = date;
            format1 = new SimpleDateFormat("yyyy.MM.dd E");
            format2 = new SimpleDateFormat("HH : mm");
            System.out.println("time update");

    }
    @Override
    public void run()
    {
        while(true) {
            try {
                gettimedata();
                this.sleep(10000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

   void gettimedata()
   {



       Calendar time = Calendar.getInstance();

       String format_time1 = format1.format(time.getTime());
       String format_time2 = format2.format(time.getTime());

       System.out.println("@@@@@@@@@@@@@@@@@@@@@"+format_time1);
       System.out.println("@@@@@@@@@@@@@@@@@@@@@"+format_time2);
       timeView.setText(format_time2);
       dateView.setText(format_time1);

   }

}

package com.example.parking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideChangeListener;

public class ActivityA extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    DisplayMetrics displayMetrics;


    Vehicle[] veh;
    LinearLayout MiddleLayout;
    TextView hidden_name, hidden_phone, hidden_time, timeView, dateView;
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


    String time_now;
    String date_now;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    Vehicle LastSelectedVehicle = new Vehicle();
    private int LastSelectedPos;

    private Slider slider;
    private BannerImageLoadingService imgloadingservice;
    private BannerSliderAdapter sliderAdapter;


    void UpdateSlider()
    {
        slider.setAdapter(new BannerSliderAdapter(this));
        slider.setSlideChangeListener(OSCL);
    }

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

        imageview.setImageBitmap(BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getAbsolutePath()
                +"/"+veh.getImagecode()+".jpg"));
        Log.d("HELPME!!",getApplicationContext().getFilesDir().getAbsolutePath()
                +veh.getImagecode()+".jpg");;
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

    void removeView(Vehicle v, int pos) {
        slider.removeViewAt(pos);
        sliderAdapter.notify();


    }

    void setCarView(LinearLayout imageLayout, LinearLayout textLayout) {

        Iterator i = FirebaseController.Vehicles.iterator();
        clearCarView(imageLayout,textLayout);
        System.out.println("CARVIEW START");
        while (i.hasNext()) {
            Vehicle veh = (Vehicle)i.next();
            addContent(ImageLayout, TextLayout, veh);
           }
        System.out.println("END");
        slider.init(imgloadingservice);
        UpdateSlider();


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



        slider = (Slider)findViewById(R.id.bannerslider);

        imgloadingservice = new BannerImageLoadingService(this);

        slider.setSlideChangeListener(OSCL);


        Resources resources = getApplicationContext().getResources();
        displayMetrics = new DisplayMetrics();
        displayMetrics = resources.getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        LI = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


        hidden_name = (TextView) findViewById(R.id.name_textview);
        hidden_phone = (TextView) findViewById(R.id.phone_textview);
        hidden_time = (TextView) findViewById(R.id.time_textview);

        SearchButton = (ImageButton) findViewById(R.id.search_button7);
        SearchEditText = (EditText) findViewById(R.id.search_edit_text7);

        SearchEditText.addTextChangedListener(this);


        TimeManager tm = new TimeManager(timeView, dateView);
        tm.start();

    }

    OnSlideChangeListener OSCL = new OnSlideChangeListener()
    {
        @Override
        public void onSlideChange(int position){
            if(FirebaseController.Vehicles.size()!=0)
            {
                LastSelectedVehicle = FirebaseController.Vehicles.get(position);
                LastSelectedPos = position;
                VehicleSelect(LastSelectedVehicle);
            }

        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent m) {

        Log.d("ImageSlider", ""+slider.selectedSlidePosition);

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
          if (diffX > pushSensitivity) {
                //NEXT

                startActivity(new Intent(this, ActivityB.class));
                Animatoo.animateSplit(this);
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
        if(veh==null)
        {
            Toast.makeText(this,"Such Vehicle doesn't exist",Toast.LENGTH_LONG);
            return ;
        }
        veh.iv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
        hidden_name.setText(veh.getName());
        hidden_phone.setText(veh.getContact());
        hidden_time.setText(Integer.toString(veh.getMinutes()));
        MiddleLayout.setVisibility(View.VISIBLE);
    }





    public void ShowCarInfo(View v)
    {
        iv2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
        new Vehicle().ShowCarInfo(LastSelectedVehicle,this);
    }

    public void intentA(View view) {

        showToast("Current Screen");

    }

    public void addNewCar(View view)
    {
        iv1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Option");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL: //Manual Input
                        ManualCarIn();
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
    {



        /*Vehicle newV = LastSelectedVehicle.randomcarGen();
        newV.setFair(Settings.hour_fair);

        FirebaseController.addVehicle(newV);
        addContent(ImageLayout,TextLayout,newV);
*/
    }
    public void intentB(View view) {

        startActivity(new Intent(this,ActivityB.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }
    public void CarOut(View view)  {
        iv4.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
        iv4.setColorFilter(Color.YELLOW);
        FirebaseController.removeVehicleCurrent(LastSelectedVehicle);

        Income income = new Income(LastSelectedVehicle);
        UpdateSlider();
        //removeView(LastSelectedVehicle,LastSelectedPos);

        FirebaseController.addIncome(income);


        iv4.setColorFilter(Color.WHITE);
    }
    public void sendSMS(View view)
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PrinterStart(View view)  {

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
    private final TimeHandler mTimeHandler = new TimeHandler(this);

    private static class TimeHandler extends Handler {
        private final WeakReference<ActivityA> mActivity;
        public TimeHandler(ActivityA activity) {
            mActivity = new WeakReference<ActivityA>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityA activity = mActivity.get();
            if (activity != null) {

                activity.handleMessage(msg);
            }
        }
    }

    // Handler 에서 호출하는 함수
    private void handleMessage(Message msg) {
        timeView.setText(time_now);
        dateView.setText(date_now);
    }



    public void toSecond(View view)
    {
        startActivity(new Intent(this,ActivityB.class));
        Animatoo.animateZoom(this);
    }

    public void toThird(View view)
    {
        startActivity(new Intent(this,ActivityC.class));
        Animatoo.animateZoom(this);
    }



    class TimeManager extends Thread implements Runnable
    {


        TextView timeView,dateView;
        SimpleDateFormat format1;
        SimpleDateFormat format2;
        TimeManager(TextView time, TextView date)
        {
            Log.d("Thread","Time Begin");
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
                    this.sleep(10000);
                    gettimedata();
                    mTimeHandler.sendMessage(mTimeHandler.obtainMessage());
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
            time_now = format_time2;
            date_now = format_time1;

        }


    }

}





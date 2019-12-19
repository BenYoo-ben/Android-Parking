package com.example.parking;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideChangeListener;

public class ActivityA extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final int PERMISSION_SEND_SMS = 0;

    DisplayMetrics displayMetrics;


    Vehicle[] veh;
    LinearLayout MiddleLayout;
    TextView hidden_VN, hidden_phone, hidden_time, timeView, dateView;
    ImageView iv1, iv2, iv3, iv4;
    ImageButton SearchButton;
    EditText SearchEditText;



    float initX, diffX, pushSensitivity = 150;
    float initY, diffY;

    LayoutInflater LI;
    FrameLayout framelayout;


    String time_now;
    String date_now;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    Vehicle LastSelectedVehicle = null;
    private int LastSelectedPos;

    private Slider slider;
    private BannerImageLoadingService imgloadingservice;
    private BannerSliderAdapter sliderAdapter;

    ManualInput MI;

    EditText Med1,Med2,Med3;
    ImageView ManualConfirmButton;
    PopupWindow popupWindow;

    private int BackButtonCheck=0;
    private int reqCode=7121;

    void UpdateSlider()
    {
        slider.setAdapter(new BannerSliderAdapter(this));
        slider.setSlideChangeListener(OSCL);
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

    void setCarView() {

        Iterator i = FirebaseController.Vehicles.iterator();

        System.out.println("CARVIEW START");
        while (i.hasNext()) {
            Vehicle veh = (Vehicle)i.next();

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
        setCarView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater LI = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View Layout = LI.inflate(R.layout.manual_layout, null);
        //setContentView(R.layout.manual_layout);

        popupWindow = new PopupWindow(Layout, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        setContentView(Layout);


        Med1 = (EditText)findViewById(R.id.mled1);
        Med2 = (EditText)findViewById(R.id.mled2);
        Med3 = (EditText)findViewById(R.id.mled3);
        ManualConfirmButton = (ImageView)findViewById(R.id.manual_confirm_button);

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


        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);





        hidden_VN = (TextView) findViewById(R.id.name_textview);
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


                BackButtonCheck=0;






            initX = m.getRawX();
            initY = m.getRawY();
            if (initY < MiddleLayout.getY() || initY > MiddleLayout.getY() + (0.1) * displayMetrics.heightPixels) {
                MiddleLayout.setVisibility(View.GONE);
            }
        return super.onTouchEvent(m);
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
                startActivity(new Intent(this, SettingsScreen.class));
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

        setCarView();

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
            Toast.makeText(this,"Such Vehicle doesn't exist",Toast.LENGTH_LONG).show();
            return ;
        }

        hidden_VN.setText(veh.getVehicle_num());
        hidden_phone.setText(veh.getContact());
        hidden_time.setText(Integer.toString(veh.getMinutes()));
        MiddleLayout.setVisibility(View.VISIBLE);
    }





    public void ShowCarInfo(View v)
    {
        iv3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
        new Vehicle().ShowCarInfo(LastSelectedVehicle,this,this);
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

                if( SettingsScreen.autosmson==1)
                startActivityForResult(a,reqCode);
                else
                    startActivity(a);
            }
        };
        builder.setMessage("Manually / Camera");
        builder.setNeutralButton("Manual", dialogClickListener);
        builder.setPositiveButton("Camera", dialogClickListener);




        builder.show();

/*
        Vehicle newV = LastSelectedVehicle.randomcarGen();
                newV.setFair(SettingsScreen.hourlyfair);

            FirebaseController.addVehicle(newV);
            addContent(ImageLayouts,TextLayouts,newV);

  */
    }
    protected void ManualCarIn()
    {
        popupWindow.showAtLocation(slider,Gravity.CENTER,0,0);


        /*Vehicle newV = LastSelectedVehicle.randomcarGen();
        newV.setFair(SettingsScreen.hourlyfair);

        FirebaseController.addVehicle(newV);
        addContent(ImageLayout,TextLayout,newV);
*/
    }

    public void CarOut(View view)  {


        if(LastSelectedVehicle!=null)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Kick this Car out?");
            builder.setIcon(R.mipmap.alert);

            final Context tmpContext = this;
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){


                            iv4.startAnimation(AnimationUtils.loadAnimation(tmpContext,R.anim.image_click));

                            FirebaseController.removeVehicleCurrent(LastSelectedVehicle);

                            Income income = new Income(LastSelectedVehicle);
                            UpdateSlider();
                            //removeView(LastSelectedVehicle,LastSelectedPos);

                            FirebaseController.addIncome(income);
                        }
                    });
            AlertDialog dialog=builder.create();
            dialog.show();
        }





    }

    public void sendSMS(Vehicle V)
    {
        Intent intent = new Intent(getApplicationContext(),ActivityA.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,PERMISSION_SEND_SMS);

//Get the SmsManager instance and call the sendTextMessage method to send message


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    0);
        }
        else
        {
            SmsManager sms=SmsManager.getDefault();

            String content = makeSMSText(V);
            String num = V.getContact().replaceAll("-","");
            Log.d("test",content);
            sms.sendTextMessage(num, null, content, pi,null);
            Toast.makeText(this,"문자 발송 완료.",Toast.LENGTH_LONG).show();
        }
    }

    public void sendSMS(View view)
    {
        iv2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
       sendSMS(LastSelectedVehicle);


    }
    private String makeSMSText(Vehicle v)
    {
        String SMS="";
        SMS = SMS.concat("차량번호 : "+v.getVehicle_num());
        SMS =SMS.concat("\n주차시작 : "+v.getArrival_time());
        SMS =SMS.concat("\n금액(시간) : "+v.getFair());
        return SMS;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PrinterStart(View view)  {

        //need Change
      PDFPrint pdfp = new PDFPrint("test_pdf.pdf",this,LastSelectedVehicle);

    }

    public Vehicle SearchVehicleWithNum(String query)
    {
        Iterator<Vehicle> i = FirebaseController.Vehicles.iterator();
        int loc=0;
       while(i.hasNext())
       {

           Vehicle veh =  (Vehicle)i.next();

           if(veh.getVehicle_num().equals(query))
           {
               VehicleSelect(veh);
               slider.setSelectedSlide(loc);
               return veh;
           }
           loc++;
       }

       Toast.makeText(getApplicationContext(),"해당 번호의 차량이 없습니다.",Toast.LENGTH_SHORT).show();
       SearchEditText.setText("");
       return null;

    }

    public void Search(View view)
    {
        SearchVehicleWithNum(SearchEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        ++BackButtonCheck;
        Toast.makeText(this,"종료를 원하시면 한번 더 눌러주세요.",Toast.LENGTH_SHORT).show();
        if (BackButtonCheck == 2){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
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
        startActivity(new Intent(this,SettingsScreen.class));
        Animatoo.animateZoom(this);
    }

    public void Manual_Confirm_Event(View view) {
        if (ManualConfirmButton == null) Log.d("FUCK","Fuck");
        ManualConfirmButton.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
        if(Med1.getText().toString().length()!=0&& Med2.getText().toString().length()!=0&&Med3.getText().toString().length()!=0)
        {
            Vehicle newV = new Vehicle(SettingsScreen.location,Med3.getText().toString(),Med1.getText().toString(),Med2.getText().toString(), new Timer().SDF.format(Calendar.getInstance().getTime()),-1);
            FirebaseController.addVehicle(newV);
            Med1.setText("");
            Med2.setText("");
           Med3.setText("");
           popupWindow.dismiss();
            setCarView();

        }
        else
        {
            Toast.makeText(this,"모든 부분에 값을 입력해주세요.",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == reqCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Result: " + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                sendSMS(new Vehicle(data.getStringExtra("location"),data.getStringExtra("name"),data.getStringExtra("contact"),data.getStringExtra("vehicle_num")
                ,data.getStringExtra("arrival_time"),data.getLongExtra("imagecode",0)));

            } else {   // RESULT_CANCEL
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
//        } else if (requestCode == REQUEST_ANOTHER) {
//            ...
        }
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





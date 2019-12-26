package com.example.parking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class ActivityB extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    float initX,diffX, pushSensitivity = 150;
    float initY,diffY;

    LinearLayout scrollLinear;
    LayoutInflater LI;
    FrameLayout framelayout;

    DisplayMetrics displayMetrics;
    TextView IncomeSumTextView;
    TextView MonthView;
    TextView IncomeTitleView;
    String IncomeSumText;
    ImageView MonthLeft;
    ImageView MonthRight;
    ImageView CalendarIV;

    Calendar DateBegin = Calendar.getInstance();
    Calendar DateEnd = Calendar.getInstance();
    int tmpyear;
    int tmpmonth;
    int DateSwitchValue=0;

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    //스크롤 뷰에 항목추가(수익목록)
    int addContent(LinearLayout textLayout,Income i)
    {

        LinearLayout ll = new LinearLayout(getApplicationContext());
        float pixels1, pixels2;

        TextView textview1 = new TextView(this);
        TextView textview2 = new TextView(this);
        TextView textview3 = new TextView(this);

        Timer t = new Timer();

        Calendar c = Calendar.getInstance();
        try {
            Date d = t.SDF.parse(i.getDeparture_time());
            c.setTime(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        textview1.setText(t.date.format(c.getTime())+"\n"+t.time.format(c.getTime()));
        textview2.setText(i.getVehicle_num());

        textview3.setText( new String("$ "+(i.getMinutes()/60)*i.getFair()));

        pixels1 = convertDpToPixel(10f,getApplicationContext());
        pixels2 = convertDpToPixel(8f,getApplicationContext());

        Typeface tf1 = ResourcesCompat.getFont(this,R.font.kenteken);
        Typeface tf2 = ResourcesCompat.getFont(this,R.font.digital7);
        textview1.setTypeface(tf2);
        textview1.setTextColor(Color.WHITE);
        textview1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        textview3.setTypeface(tf2);
        textview3.setTextColor(Color.WHITE);
        textview3.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        textview2.setTypeface(tf1);
        textview2.setTextColor(Color.WHITE);
        textview2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        LinearLayout.LayoutParams tlp =new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        tlp.setMargins(0,0,0,0);
        ll.setLayoutParams(tlp);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        tlp =new  LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2f);
        textview1.setLayoutParams(tlp);
        textview1.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textview1.requestLayout();

        tlp =new  LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3f);
        textview2.setLayoutParams(tlp);
        textview2.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textview2.requestLayout();

        tlp =new  LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2f);
        textview3.setLayoutParams(tlp);
        textview3.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textview3.requestLayout();

       ll.addView(textview1);
       ll.addView(textview2);
       ll.addView(textview3);

        textview1.setTextSize(pixels1);
        textview2.setTextSize(pixels2);
        textview3.setTextSize(pixels2);

        textview1.setGravity(Gravity.CENTER);
        textview2.setGravity(Gravity.CENTER);
        textview3.setGravity(Gravity.CENTER);



        ll.setBackgroundResource(R.drawable.image_border2);

        textview2.setOnClickListener(this);

        textLayout.addView(ll);

        return 1;
    }

    //토스트 표시
    private void showToast(String message)
    {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
    }
    //수익목록 초기화 및 재배치
    private long setIncomeView(LinearLayout textLayout)
    {

        scrollLinear.removeAllViews();
        long money_sum = 0;
        Iterator i = FirebaseController.Incomes.iterator();
        System.out.println("INCOMEVIEW START");

        try {
            Log.d("IncomeList",new Timer().SDF.format(DateBegin.getTime())+" ~ "+new Timer().SDF.format(DateEnd.getTime()));
            Log.d("IncomeList",DateBegin.getTimeInMillis()+" ~ "+DateEnd.getTimeInMillis());
         while (i.hasNext()) {
            Income income = (Income)i.next();
            Calendar IncomeTime = Calendar.getInstance();
            IncomeTime.setTime(new Timer().SDF.parse(income.getDeparture_time()));

            if(IncomeTime.getTimeInMillis() > DateBegin.getTimeInMillis()
            && IncomeTime.getTimeInMillis() < DateEnd.getTimeInMillis())
            {
                Log.d("IncomeList","Sure in List : "+income.getDeparture_time());

                addContent(scrollLinear,income);
                money_sum += (income.getMinutes()/60)*income.getFair();

            }
            else
            {
                Log.d("IncomeList","Not in List : "+income.getDeparture_time()+"  = "+IncomeTime.getTimeInMillis());
            }
           }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("END");
        TextRandom tr = new TextRandom(money_sum,IncomeSumTextView);
        tr.start();
        return money_sum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

       displayMetrics = new DisplayMetrics();

        LI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IncomeSumTextView = (TextView)findViewById(R.id.income_sum);

        framelayout = new FrameLayout(this);
        scrollLinear = (LinearLayout)(findViewById(R.id.scrollLinear));
        MonthView = (TextView)findViewById(R.id.month_text_view);
        MonthLeft = (ImageView)findViewById(R.id.month_left);
        MonthRight = (ImageView)findViewById(R.id.month_right);
        CalendarIV = (ImageView)findViewById(R.id.calendar_iv);
        IncomeTitleView = (TextView)findViewById(R.id.income_title);

        MonthLeft.setOnClickListener(this);
        MonthRight.setOnClickListener(this);
        CalendarIV.setOnClickListener(this);


        DateBegin.set(DateEnd.getTime().getYear()+1900, DateEnd.getTime().getMonth(),1,0,0);
        //DateBegin.set(2019,11,4);
        DateEnd.set(Calendar.getInstance().getTime().getYear()+1900,DateBegin.getTime().getMonth(),
                DateBegin.getActualMaximum(DateBegin.DATE),0,0);

        tmpyear = DateEnd.getTime().getYear()+1900;
        tmpmonth = DateEnd.getTime().getMonth()+1;
        if(tmpmonth<10)
            MonthView.setText(" "+tmpyear+" - 0"+tmpmonth+" ");
        else
        MonthView.setText(" "+tmpyear+" - "+tmpmonth+" ");



    }

    //화면 좌우 이동.
    @Override
    public boolean onTouchEvent(MotionEvent m)
    {
        if(m.getAction() == MotionEvent.ACTION_DOWN) {
            initX = m.getRawX();
            initY = m.getRawY();
        }
        if(m.getAction()==MotionEvent.ACTION_UP)
        {
            diffY = initY - m.getRawY();
            diffX = initX - m.getRawX();
           if(diffX > pushSensitivity)
            {
                //NEXT
                startActivity(new Intent(this,SettingsScreen.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                return super.onTouchEvent(m);
            }
            else if(diffX < -(pushSensitivity*2))
            {
                //Previous
                startActivity(new Intent(this,ActivityA.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                return super.onTouchEvent(m);
            }

        }
        return super.onTouchEvent(m);
    }

    //시작시와 resume시 스크롤뷰 재배치.
    @Override
    public void onStart()
    {
        super.onStart();
        setIncomeView(scrollLinear);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        setIncomeView(scrollLinear);
    }

    @Override
    public void onClick(View view) {

        //달력버튼이 선택되면 DatePickerDialog로 두개의 날짜를 고르도록 함.
        if(view==CalendarIV)
        {
            CalendarIV.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
            DatePickerDialog dialog = new DatePickerDialog(this, this, DateEnd.getTime().getYear()+1900,DateEnd.getTime().getMonth()
            , DateEnd.getTime().getDay());
            dialog.show();



            return;
        }

        //월변경 이전달로
        if(view==MonthLeft) {
            MonthLeft.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
            IncomeTitleView.setTextSize(10 * Resources.getSystem().getDisplayMetrics().density);
            IncomeTitleView.setText("Monthly Income");
            int month = Integer.parseInt(MonthView.getText().toString().substring(8, 10));
            int year = Integer.parseInt(MonthView.getText().toString().substring(1, 5));

            month = month - 1;


            if (month == 0) {
                month = 12;
                year = year - 1;
            }
            if (month >= 10) {
                MonthView.setText(" " + year + " - " + month + " ");
            } else {
                MonthView.setText(" " + year + " - 0" + month + " ");
            }

            DateBegin.add(DateBegin.MONTH, -1);
            DateEnd.add(DateEnd.MONTH, -1);
            setIncomeView(scrollLinear);

            return;
        }//월변경 다음달로
        else if(view == MonthRight)
        {
            MonthRight.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_click));
            IncomeTitleView.setTextSize(10 * Resources.getSystem().getDisplayMetrics().density);
            IncomeTitleView.setText("Monthly Income");
            int month = Integer.parseInt(MonthView.getText().toString().substring(8,10));
            int year = Integer.parseInt(MonthView.getText().toString().substring(1,5));

            month=month+1;
            if(month==13)
            {
                month=1;
                year=year+1;
            }
            if(month>=10)
            {
                MonthView.setText(" "+year+" - "+month+" ");
            }
            else
            {
                MonthView.setText(" "+year+" - 0"+month+" ");
            }

            DateBegin.add(DateBegin.MONTH,+1);
            DateEnd.add(DateEnd.MONTH,+1);
            setIncomeView(scrollLinear);

            return ;
        }
        //수익목록의 아이템이 선택되면 자세한 정보를 표시
        if(view!=null) {

            Iterator<Income> i = FirebaseController.Incomes.iterator();
            Income TI;

            while (i.hasNext()) {

                if (  (TI = i.next()).getVehicle_num().equals(((TextView) view).getText())) {
                    new Income().ShowIncomeInfo(TI,this);
                }
            }

            return ;
        }
    }







    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        try {
        String dateString = year-2000+"-"+(month+1)+"-"+dayOfMonth;
       // Toast.makeText(getApplicationContext(), year + "년" + month + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), dateString, Toast.LENGTH_SHORT).show();
            month=month+1;

        if(DateSwitchValue==0) {
            dateString+=" "+"00:00";
            Log.d("Date!",dateString);
                DateBegin.setTime(new Timer().SDF.parse(dateString));

            DatePickerDialog dialog = new DatePickerDialog(this, this, DateBegin.getTime().getYear()+1900,
                    DateBegin.getTime().getMonth()+1, DateBegin.getTime().getDay());
            dialog.show();
            DateSwitchValue++;
        }else
        {
            dateString+=" "+"23:59";
            Log.d("Date!",dateString);
            DateEnd.setTime(new Timer().SDF.parse(dateString));
            DateSwitchValue--;
            //DP TO PX
             IncomeTitleView.setTextSize(10 * Resources.getSystem().getDisplayMetrics().density);

             IncomeTitleView.setText(new Timer().date.format(DateBegin.getTime())+" ~ "+new Timer().date.format(DateEnd.getTime()));

             if(month<10)
                 MonthView.setText(" "+year+" - 0"+month+" ");
             else
                 MonthView.setText(" "+year+" - "+month+" ");

                 setIncomeView(scrollLinear);


        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //약한참조를 통해 돈이 토글되는 애니메이션 구현(메모리 누수 방지)
    private final TextHandler mTextHandler = new TextHandler(this);
    private static class TextHandler extends Handler {
        private final WeakReference<ActivityB> mActivity;
        public TextHandler(ActivityB activity) {
            mActivity = new WeakReference<ActivityB>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityB activity = mActivity.get();
            if (activity != null) {

                activity.handleMessage(msg);
            }
        }
    }

    // Handler 에서 호출하는 함수
    private void handleMessage(Message msg)
    {
        IncomeSumTextView.setText(IncomeSumText);
    }


    public void toFirst(View view)
    {
        startActivity(new Intent(this,ActivityA.class));
        Animatoo.animateZoom(this);
    }


    public void toThird(View view)
    {
        startActivity(new Intent(this,SettingsScreen.class));
        Animatoo.animateZoom(this);
    }

    //수익 총 합계 토글시킨 뒤 표시.
    class TextRandom extends Thread implements Runnable
    {
        long sum;
        TextView tv;
        int imax=100;
        TextRandom(long sum, TextView tv)
        {

            this.sum = sum;
            this.tv = tv;
        }
        @Override
        public void run()
        {


                    for(int i= 0;i<imax;i++)
                    {
                        try {
                            this.sleep(5);
                            IncomeSumText="$ "+(int)(Math.random()*sum);
                            mTextHandler.sendMessage(mTextHandler.obtainMessage());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
            IncomeSumText="$ "+sum;
            mTextHandler.sendMessage(mTextHandler.obtainMessage());
        }

    }

}

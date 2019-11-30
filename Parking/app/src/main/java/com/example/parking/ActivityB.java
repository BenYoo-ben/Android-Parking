package com.example.parking;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class ActivityB extends AppCompatActivity implements View.OnClickListener {

    float initX,diffX, pushSensitivity = 150;
    float initY,diffY;

    LinearLayout scrollLinear;
    LayoutInflater LI;
    FrameLayout framelayout;
    PopupWindow popup;
    DisplayMetrics displayMetrics;
    TextView IncomeSumTextView;

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
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
        textview3.setText("$ "+i.getMinutes()*Settings.hour_fair);

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
       textLayout.addView(ll);

        return 1;
    }

    private void showToast(String message)
    {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
    }
    private long setIncomeView(LinearLayout textLayout)
    {
        long money_sum = 0;
        Iterator i = FirebaseController.Incomes.iterator();
        System.out.println("INCOMEVIEW START");
        while (i.hasNext()) {
            Income income = (Income)i.next();
           addContent(scrollLinear,income);
           money_sum += (income.getMinutes()/60)*Settings.hour_fair;
        }
        System.out.println("END");
        IncomeSumTextView.setText("$ "+money_sum);
        return money_sum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

       displayMetrics = new DisplayMetrics();

        LI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IncomeSumTextView = (TextView)findViewById(R.id.income_sum);
        popup = new PopupWindow(LI.inflate(R.layout.top_popup,null,false), ActionBar.LayoutParams.MATCH_PARENT,150,true);
        popup.setAnimationStyle(R.anim.slide_down);
        framelayout = new FrameLayout(this);
        scrollLinear = (LinearLayout)(findViewById(R.id.scrollLinear));

    }

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
            if(diffY < -pushSensitivity)
            {
                showToast("WORKED?");
                popup.showAtLocation(framelayout, Gravity.TOP,0,0);

                return super.onTouchEvent(m);
            }
            else if(diffX > pushSensitivity)
            {
                //NEXT
                startActivity(new Intent(this,ActivityC.class));
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

    @Override
    public void onStart()
    {
        super.onStart();
        setIncomeView(scrollLinear);

    }


    @Override
    public void onClick(View view) {
    }
    public void intentA(View view)
    {
        startActivity(new Intent(this,ActivityA.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    public void intentB(View view)
    {
        showToast("current Screen");
    }
    public void intentC(View view)
    {
        startActivity(new Intent(this,ActivityC.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    public void intentD(View view)
    {
        startActivity(new Intent(this,ActivityD.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}

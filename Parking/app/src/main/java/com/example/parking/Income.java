package com.example.parking;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Income {
    //수익항목 클래스
    private int income;

    private String departure_time;
    private String arrival_time;
    private int minutes;
    private String vehicle_num;
    private String ID;
    private int fair;




    public String getDeparture_time()
    {
        return departure_time;
    }
    public String getArrival_time()
    {
        return arrival_time;
    }
    public int getMinutes()
    {
        return minutes;
    }
    public String getVehicle_num()
    {
        return vehicle_num;
    }
    public String getID(){  return ID; }
    public int getFair(){return fair;}

    public void setDeparture_time(String departure_time){this.departure_time = departure_time;}
    public void setArrival_time(String arrival_time){this.arrival_time=arrival_time;}
    public void setMinutes(int minutes){this.minutes = minutes;}
    public void setVehicle_num(String vehicle_num){this.vehicle_num=vehicle_num;}
    public void setID(String ID){this.ID=ID;}
    public void setFair(int fair){this.fair=fair;}

    public Income(Vehicle LastSelectedVehicle)
    {
       setVehicle_num(LastSelectedVehicle.getVehicle_num());
       setArrival_time(LastSelectedVehicle.getArrival_time());
        Departuretimenow();
       CalcMinutes();
       IDgen();
       setFair(LastSelectedVehicle.getFair());
    }
    public Income(){}

    //각 항목을 구별할 ID 생성
    void IDgen()
    {
        this.ID = departure_time+vehicle_num;
    }

    //출차시간 저장
    public void Departuretimenow()
    {
        SimpleDateFormat SF = new SimpleDateFormat("yy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        this.departure_time = SF.format(c.getTime());
    }

    //시간 계산
    public void CalcMinutes()
    {
        try {
            Calendar time_now = Calendar.getInstance();
            Calendar time_t = Calendar.getInstance();


            Date date = new Timer().SDF.parse(arrival_time);
            time_t.setTime(date);
            //time_now.add(time_now.YEA);
            time_now.getTimeInMillis();
            minutes = (int)((time_now.getTimeInMillis() - time_t.getTimeInMillis()) / (1000 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //항목 클릭시 세부정보보여주기
    public void ShowIncomeInfo(Income i, Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(i.getID()+" - 정보");
        builder.setMessage("CarNo : "+i.getVehicle_num()+"\nArrival : "+i.getArrival_time()+"\nDeparture : "
                +i.getDeparture_time()+"\nHourFee : "+i.getFair()+"\nMinutes : "+i.getMinutes());

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

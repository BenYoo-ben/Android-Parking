package com.example.parking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Income {
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

    void IDgen()
    {
        this.ID = departure_time+vehicle_num;
    }

    public void Departuretimenow()
    {
        SimpleDateFormat SF = new SimpleDateFormat("yy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        this.departure_time = SF.format(c.getTime());
    }

    public void CalcMinutes()
    {
        try {
            Calendar time_now = Calendar.getInstance();
            Calendar time_t = Calendar.getInstance();


            Date date = new Timer().SDF.parse(arrival_time);
            time_t.setTime(date);
            time_now.getTimeInMillis();
            minutes = (int)((time_now.getTimeInMillis() - time_t.getTimeInMillis()) / 1000 / 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

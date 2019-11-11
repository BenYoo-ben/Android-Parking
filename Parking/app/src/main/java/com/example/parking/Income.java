package com.example.parking;

public class Income {
    private int income;

    private String departure_time;
    private String arrival_time;
    private int minutes;
    private String vehicle_num;
    private String ID;




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
    public String getID()
    {
        return ID;
    }

    void IDgen()
    {
        ID = arrival_time+departure_time+vehicle_num;
    }

}

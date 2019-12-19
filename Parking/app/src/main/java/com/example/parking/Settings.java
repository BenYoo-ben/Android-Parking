package com.example.parking;

public class Settings  {



     int hourlyfair ;
    String contact;

    //static String imgloc =
    String location;
    int autosmson;


    public int getHourlyfair(){return hourlyfair;}
    public String getContact(){return contact;}
    public String getLocation(){return location;}
    public int getAutosmson(){return autosmson;}

    public void setHourlyfair(int hourlyfair){this.hourlyfair = hourlyfair;}
    public void setContact(String contact){this.contact = contact;}
    public void setLocation(String location){ this.location =location; }
    public void setAutosmson(int autosmson){this.autosmson = autosmson;}

}

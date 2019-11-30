package com.example.parking;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.SimpleFormatter;

public class Vehicle
    {



        private String location = null;
        private String name = null;
        private String contact = null;
        private String vehicle_num = null;
        private String arrival_time = null;
        // ex  "11/03/14 09:29:58";

        private int minutes=0;
        private long imagecode;
        private int fair;
        public ImageView iv;
        public TextView tv;


        public String getLocation()
        {
            return location;
        }

        public String getName()
        {
            return name;
        }
        public String getContact()
        {
            return contact;
        }
        public String getVehicle_num()
        {
            return vehicle_num;
        }
        public String getArrival_time()
        {
            return arrival_time;
        }
        public int getMinutes()
        {
            return minutes;
        }
        public long getImagecode()
        {
            return imagecode;
        }
        public int getFair(){return fair;}

        public void setLocation(String location){ this.location =location; }
        public void setName(String name){ this.name =name; }
        public void setContact(String contact){ this.contact=contact; }
        public void setVehicle_num(String vehicle_num){ this.vehicle_num =vehicle_num; }
        public void setArrival_time(String arrival_time){ this.arrival_time =arrival_time; }
        public void setMinutes(int minutes){ this.minutes=minutes; }
        public void setImagecode(long imagecode){this.imagecode = imagecode;}
        public void setFair(int fair){this.fair=fair;}


        public void Arrivaltimenow()
        {


            Calendar c = Calendar.getInstance();
            this.arrival_time =   new Timer().SDF.format(c.getTime());
        }


       public Vehicle(){}

       public Vehicle(String location,String name,String contact,String vehicle_num,String arrival_time,long imagecode)
       {
           setLocation(location);
           setName(name);
           setContact(contact);
           setVehicle_num(vehicle_num);
           setArrival_time(arrival_time);
           setMinutes(0);
           setImagecode(imagecode);
           setFair(Settings.hour_fair);
       }

        Vehicle(int num)
        {
            this.vehicle_num=Integer.toString(num);
        }

        public void ManualInput(){}



        void VehiclePrint()
        {

            System.out.println();
            System.out.println("location :"+location);
            System.out.println("name :"+name);
            System.out.println("contact :"+contact);
            System.out.println("vehicle_num :"+vehicle_num);
            System.out.println("arrival_time :"+arrival_time);
            System.out.println("imagecode :"+imagecode);

        }
        void VehiclePrint(Vehicle v)
        {

            System.out.println();
            System.out.println("location :"+v.location);
            System.out.println("name :"+v.name);
            System.out.println("contact :"+v.contact);
            System.out.println("vehicle_num :"+v.vehicle_num);
            System.out.println("arrival_time :"+v.arrival_time);
            System.out.println("imagecode :"+v.imagecode);

        }
        void VehicleVectorPrint(Vector<Vehicle> V)
        {
            Iterator i = V.iterator();

            while (i.hasNext())
            {
                VehiclePrint((Vehicle)i.next());
                System.out.println();
            }
        }



        Vehicle randomcarGen()
        {
                Vehicle newV = new Vehicle();

                int test_int = (int)(Math.random()*255);
            newV.location = Integer.toString(test_int);
            newV.name =Integer.toString(test_int);
            newV.contact = Integer.toString(test_int);
            newV.vehicle_num = Integer.toString(test_int);
            newV.imagecode = (int)(Math.random()*16777215) + 0xFF000000;
            newV.Arrivaltimenow();
            newV.fair = Settings.hour_fair;


            return newV;
        }

//Vector Storage DB 와  Vector Current DB



}

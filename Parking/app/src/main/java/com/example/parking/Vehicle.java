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
        private String vehicle_type = null;
        private String arrival_time = null;
        // ex  "11/03/14 09:29:58";

        private int minutes=0;
        private int color;
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
        public String getVehicle_type()
        {
            return vehicle_type;
        }
        public String getArrival_time()
        {
            return arrival_time;
        }
        public int getMinutes()
        {
            return minutes;
        }
        public int getColor()
        {
            return color;
        }
        public int getFair(){return fair;}

        public void setLocation(String location){ this.location =location; }
        public void setName(String name){ this.name =name; }
        public void setContact(String contact){ this.contact=contact; }
        public void setVehicle_num(String vehicle_num){ this.vehicle_num =vehicle_num; }
        public void setVehicle_type(String vehicle_type){ this.vehicle_type =vehicle_type; }
        public void setArrival_time(String arrival_time){ this.arrival_time =arrival_time; }
        public void setMinutes(int minutes){ this.minutes=minutes; }
        public void setColor(int color){this.color = color;}
        public void setFair(int fair){this.fair=fair;}


        public void Arrivaltimenow()
        {
            SimpleDateFormat SF = new SimpleDateFormat("yy-MM-dd HH:mm");
            Calendar c = Calendar.getInstance();
            this.arrival_time = SF.format(c.getTime());
        }


       public Vehicle(){}
        Vehicle(int num)
        {
            this.vehicle_num=Integer.toString(num);
        }
        public Vehicle(String vehicle_num,String name, String contact,String location,String vehicle_type,int color, String arrival_time)
        {
            this.location = location;
            this.name =name;
            this.contact = contact;
            this.vehicle_num = vehicle_num;
            this.vehicle_type = vehicle_type;
            this.arrival_time = arrival_time;
            this.color =color;
        }
        public Vehicle(String location,String name,String contact,String vehicle_num, String vehicle_type,int minutes,int color)
        {
            this.location = location;
            this.name =name;
            this.contact = contact;
            this.vehicle_num = vehicle_num;
            this.vehicle_type = vehicle_type;
            this.minutes =minutes;
            this.color =color;
        }

        public void ManualInput(){}



        void VehiclePrint()
        {

            System.out.println();
            System.out.println("vehicle_type :"+vehicle_type);
            System.out.println("location :"+location);
            System.out.println("name :"+name);
            System.out.println("contact :"+contact);
            System.out.println("vehicle_num :"+vehicle_num);
            System.out.println("arrival_time :"+arrival_time);
            System.out.println("color :"+color);

        }
        void VehiclePrint(Vehicle v)
        {

            System.out.println();
            System.out.println("vehicle_type :"+v.vehicle_type);
            System.out.println("location :"+v.location);
            System.out.println("name :"+v.name);
            System.out.println("contact :"+v.contact);
            System.out.println("vehicle_num :"+v.vehicle_num);
            System.out.println("arrival_time :"+v.arrival_time);
            System.out.println("color :"+v.color);

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
            newV.vehicle_type = Integer.toString(test_int);
            newV.color = (int)(Math.random()*16777215) + 0xFF000000;
            newV.Arrivaltimenow();



            return newV;
        }

//Vector Storage DB 와  Vector Current DB



}

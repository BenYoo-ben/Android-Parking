package com.example.parking;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

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


        //현재 입차시간 저장
        protected void Arrivaltimenow()
        {


            Calendar c = Calendar.getInstance();
            this.arrival_time =   new Timer().SDF.format(c.getTime());
        }


       public Vehicle(){}

       //차량 추가
       public Vehicle(String location,String name,String contact,String vehicle_num,String arrival_time,long imagecode)
       {
           setLocation(location);
           setName(name);
           setContact(contact);
           setVehicle_num(vehicle_num);
           setArrival_time(arrival_time);
           setMinutes(0);
           setImagecode(imagecode);
           setFair(SettingsScreen.hourlyfair);
       }

       //현재시점으로부터 지난 시간 계산
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

        //차량 정보 콘솔출력
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
        //차량 정보 콘솔출력2
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
        //벡터형식으로 되있는 차량 출력
        void VehicleVectorPrint(Vector<Vehicle> V)
        {
            Iterator i = V.iterator();

            while (i.hasNext())
            {
                VehiclePrint((Vehicle)i.next());
                System.out.println();
            }
        }

        //AlertDialog를 통한 차량 정보 표시.
        public void ShowCarInfo(Vehicle LastSelectedVehicle, Context context, android.app.Activity A)
        {

            final android.app.Activity act = A;
            final Vehicle v = LastSelectedVehicle;
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle(LastSelectedVehicle.getVehicle_num()+" - 정보");
            builder.setMessage("Name : "+LastSelectedVehicle.getName()+"\nPhone : "+LastSelectedVehicle.getContact()+"\nArrival : "+LastSelectedVehicle.getArrival_time()+"\nLocation : "+LastSelectedVehicle.getLocation()+
                    "\nimagecode : "+LastSelectedVehicle.getImagecode());

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){

                        }
                    });
            builder.setNegativeButton("Print", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PDFPrint pdfp = new PDFPrint(v.getVehicle_num()+v.getArrival_time()+".pdf",act,v);
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
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
            newV.fair = SettingsScreen.hourlyfair;


            return newV;
        }

//Vector Storage DB 와  Vector Current DB



}

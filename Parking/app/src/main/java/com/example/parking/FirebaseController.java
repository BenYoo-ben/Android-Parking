package com.example.parking;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;


public class FirebaseController extends AppCompatActivity {

    private static int initial_load = 0;
    static String userID = "Admin";

    static String databaseString;

    static Vehicle V = new Vehicle();
    static Vector<Vehicle> Vehicles = new Vector<Vehicle>();
    static Vector<Income> Incomes = new Vector<Income>();
    static double loading_state_value = 0.0;
    static int very_first_run = 0;

    static private DataSnapshot datasnap;
    static private FirebaseDatabase firebaseDatabase;
    static private DatabaseReference ref;

    //Firebase에 차량추가
    static void addVehicle(Vehicle veh) {


        ref.child(userID).child("Current").child(veh.getVehicle_num());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("location").setValue(veh.getLocation());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("name").setValue(veh.getName());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("contact").setValue(veh.getContact());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("arrival_time").setValue(veh.getArrival_time());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("imagecode").setValue(veh.getImagecode());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("vehicle_num").setValue(veh.getVehicle_num());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("fair").setValue(veh.getFair());

        Vehicles.add(veh);
    }

    //Firebase에 차량삭제
    static int removeVehicleCurrent(Vehicle veh) {
        ref.child(userID).child("Current").child(veh.getVehicle_num()).removeValue();

        int index = Vehicles.indexOf(veh);
        Vehicles.remove(index);
        return index;
    }

    //설정 변경
    static void updateSettings()
    {
        ref.child(userID).child("Settings").child(userID).child("hourlyfair").setValue(SettingsScreen.hourlyfair);
        ref.child(userID).child("Settings").child(userID).child("contact").setValue(SettingsScreen.contact);
        ref.child(userID).child("Settings").child(userID).child("location").setValue(SettingsScreen.location);
        ref.child(userID).child("Settings").child(userID).child("autosmson").setValue(SettingsScreen.autosmson);
    }

    //수익항목 추가
    static void addIncome(Income i) {
        ref.child(userID).child("Income").child(i.getID());
        ref.child(userID).child("Income").child(i.getID()).child("departure_time").setValue(i.getDeparture_time());
        ref.child(userID).child("Income").child(i.getID()).child("arrival_time").setValue(i.getArrival_time());
        ref.child(userID).child("Income").child(i.getID()).child("minutes").setValue(i.getMinutes());
        ref.child(userID).child("Income").child(i.getID()).child("vehicle_num").setValue(i.getVehicle_num());
        ref.child(userID).child("Income").child(i.getID()).child("ID").setValue(i.getID());
        ref.child(userID).child("Income").child(i.getID()).child("fair").setValue(i.getFair());
        Incomes.add(i);

    }




    //Firebase 로드/시작할시
    static void onStarter() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        System.out.println("=================="+userID+"===================");
        //사용중인 Firebase 주소
        Firebase TestNewFirebase =   new Firebase("https://my-project-1533899069285.firebaseio.com/"+userID);

        TestNewFirebase.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener(){

            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    //데이터가 존재하지 않을경우
                    System.out.println("DATA DOESN'T EXIST!!!");
                    loading_state_value = 0;
                    very_first_run=1;
                    return;
                }
                else
                {
                    System.out.println("DATA YES!!!");

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        ref = firebaseDatabase.getReference();


           ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //로딩률을 확인하기 위한 각자 Child 개수
                   long top1 = dataSnapshot.child(userID).child("Current").getChildrenCount();
                    long top2 = dataSnapshot.child(userID).child("Income").getChildrenCount();
                    long top3 = dataSnapshot.child(userID).child("Settings").getChildrenCount();

                   System.out.println("TOP1 IS >"+top1);
                    System.out.println("TOP2 IS >"+top2);
                    System.out.println("TOP32 IS >"+top3);
                   long count=0;
                    //read the state of databsae and get vehicle data and have it saved on Vehicles instance.
                    Vector<Vehicle> vehicle_vector = new Vector<Vehicle>();
                    Vector<Income> income_vector = new Vector<Income>();
                    if (initial_load == 0) {
                    if(top1!=0) {


                        Vehicles.clear();
                           for (DataSnapshot dss : dataSnapshot.child(userID).child("Current").getChildren()) {


                               Vehicle newVehicle = dss.getValue(Vehicle.class);
                               vehicle_vector.add(newVehicle);

                               ++count;
                               loading_state_value = (double) (count / (top1 + top2+top3));
                               System.out.println("value ::" + loading_state_value);


                           }
                       }
                       if (top2 != 0) {
                           for (DataSnapshot dss : dataSnapshot.child(userID).child("Income").getChildren()) {


                               Income newIncome = dss.getValue(Income.class);
                               income_vector.add(newIncome);
                               ++count;
                              loading_state_value = (double) (count / (top1 + top2+top3));
                               System.out.println("value ::" + loading_state_value);


                           }


                       }
                       if(top3!=0)
                       {
                           for (DataSnapshot dss : dataSnapshot.child(userID).child("Settings").getChildren()) {


                             Settings S = dss.getValue(Settings.class);
                               Log.d("SettingsIn","Settings:"+S.getHourlyfair());
                               SettingsScreen.obtainSettings(S);
                                Log.d("SettingsIn","Screen:"+SettingsScreen.hourlyfair);

                               ++count;
                               loading_state_value = (double) (count / (top1 + top2+top3));
                               System.out.println("value ::" + loading_state_value);


                           }


                       }

                       Vehicles = vehicle_vector;
                       Incomes = income_vector;
                        System.out.println(":::Data Snapped::: Vehicle size =" + Vehicles.size() + " Income size = " + Incomes.size());

                        initial_load++;

                        if(loading_state_value<1)
                        {
                            Log.d("FFB","very_first_run set to 1");
                            Log.d("FFB","userID = "+userID);
                            very_first_run=1;

                        }
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


}

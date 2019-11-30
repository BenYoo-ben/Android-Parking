package com.example.parking;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;


public class FirebaseController extends AppCompatActivity {

    private static int initial_load = 0;
    static String userID = "Admin";

    static String databaseString;

    static Vehicle V = new Vehicle();
    static Vector<Vehicle> Vehicles = new Vector<Vehicle>();
    static Vector<Income> Incomes = new Vector<Income>();
    static double loading_state_value = 0.0;

    static private DataSnapshot datasnap;
    static private FirebaseDatabase firebaseDatabase;
    static private DatabaseReference ref;

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

    static int removeVehicleCurrent(Vehicle veh) {
        ref.child(userID).child("Current").child(veh.getVehicle_num()).removeValue();

        int index = Vehicles.indexOf(veh);
        Vehicles.remove(index);
        return index;
    }

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





    static void onStarter() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        System.out.println("=================="+userID+"===================");

        Firebase TestNewFirebase =   new Firebase("https://my-project-1533899069285.firebaseio.com/"+userID);

        TestNewFirebase.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener(){


            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    System.out.println("DATA DOESN'T EXIST!!!");
                    loading_state_value = 1;
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

                   long top1 = dataSnapshot.child(userID).child("Current").getChildrenCount();
                    long top2 = dataSnapshot.child(userID).child("Income").getChildrenCount();
                   System.out.println("TOP1 IS >"+top1);
                    System.out.println("TOP2 IS >"+top2);
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
                               loading_state_value = (double) (count / (top1 + top2));
                               System.out.println("value ::" + loading_state_value);


                           }
                       }
                       if (top2 != 0) {
                           for (DataSnapshot dss : dataSnapshot.child(userID).child("Income").getChildren()) {


                               Income newIncome = dss.getValue(Income.class);
                               income_vector.add(newIncome);
                               ++count;
                              loading_state_value = (double) (count / (top1 + top2));
                               System.out.println("value ::" + loading_state_value);


                           }


                       }

                       System.out.println(":::Data Snapped::: Vehicle size =" + Vehicles.size() + " Income size = " + Incomes.size());
                       Vehicles = vehicle_vector;
                       Incomes = income_vector;
                       initial_load++;
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


}

package com.example.parking;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;


public class FirebaseController extends AppCompatActivity {

    private static int initial_load=0;
    static String userID="Admin";

    static String databaseString;

    static Vehicle V = new Vehicle();
    static Vector<Vehicle> Vehicles = new Vector<Vehicle>();

    static private DataSnapshot datasnap ;
    static private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    static private DatabaseReference ref = firebaseDatabase.getReference();

    static void addVehicleCurrent(Vehicle veh)
    {

        ref.child(userID).child("Current").child(veh.getVehicle_num());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("location").setValue(veh.getLocation());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("name").setValue(veh.getName());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("contact").setValue(veh.getContact());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("vehicle_type").setValue(veh.getVehicle_type());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("arrival_time").setValue(veh.getArrival_time());
          ref.child(userID).child("Current").child(veh.getVehicle_num()).child("color").setValue(veh.getColor());
        ref.child(userID).child("Current").child(veh.getVehicle_num()).child("vehicle_num").setValue(veh.getVehicle_num());

        Vehicles.add(veh);
    }

    static int removeVehicleCurrent(Vehicle veh)
    {
        ref.child(userID).child("Current").child(veh.getVehicle_num()).removeValue();

        int index =  Vehicles.indexOf(veh);
        Vehicles.remove(index);
        return index;
    }



    static void onStarter() {


           ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    datasnap = dataSnapshot;



                    //read the state of databsae and get vehicle data and have it saved on Vehicles instance.
                    if (initial_load == 0) {
                        Vector<Vehicle> vehicle_vector = new Vector<Vehicle>();
                        for (DataSnapshot dss : dataSnapshot.child(userID).child("Current").getChildren()) {


                            Vehicle newVehicle = dss.getValue(Vehicle.class);
                            vehicle_vector.add(newVehicle);

                        }
                        System.out.println(":::Data Snapped::: Vehicle size =" + Vehicles.size());
                        Vehicles = vehicle_vector;
                    initial_load++;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


}

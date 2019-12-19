package com.example.parking;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timer extends Calendar {
    SimpleDateFormat SDF = new SimpleDateFormat("yy-MM-dd HH:mm");
    SimpleDateFormat date = new SimpleDateFormat("MM/dd");
    SimpleDateFormat time = new SimpleDateFormat("HH : mm");


    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    @Override
    public void add(int field, int amount) {

    }

    @Override
    public void roll(int field, boolean up) {

    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }
}

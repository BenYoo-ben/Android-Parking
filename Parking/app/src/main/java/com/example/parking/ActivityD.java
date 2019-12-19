package com.example.parking;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityD extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
// our settings activity must come here
        Fragment fragment = new SettingsHolder();

// this fragment must be from android.app.Fragment,
// if you use support fragment, it will not work
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
// when saved instance state is null, that means
// activity is created for the first time, so basically
// add the fragment to activity if and only if activity is new
// when activity rotates, do nothing
            transaction.add(R.id.settings_holder, fragment, "settings_screen");
        }
        transaction.commit();
    }

    // below inner class is a fragment, which must be called in the main activity
    public static class SettingsHolder extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
// here we should call settings ui
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}

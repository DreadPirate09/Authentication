package com.example.list;
import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public static MyApplication singleton;

    public List<Location> myLocations = null;
    public Location last = null;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public MyApplication getInstance()
    {
        return singleton;
    }
    public void onCreate()
    {
        super.onCreate();
        singleton=this;
        myLocations=new ArrayList<>();
    }


    public Location getMyLocation() {
        last = myLocations.get(myLocations.size()-1);
    return last;
}}




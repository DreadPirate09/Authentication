package com.example.list;

import android.location.Location;

public class Coordonates {
    private Location lastLocation;
    private String name;
    public Coordonates(Location lastLocation)
    {
      this.lastLocation=lastLocation;
    }
}

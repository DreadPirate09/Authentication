package com.example.list;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.list.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    List<Location> savedLocation = null;
    private final Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("break here?");

        com.example.list.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        System.out.println("break here?");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;

        System.out.println("break here?");
        mapFragment.getMapAsync(this);
        MyApplication myApplication =(MyApplication)getApplicationContext();

        System.out.println("break here?");
        savedLocation = myApplication.getMyLocations();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        LatLng sydney = new LatLng(45.2, 21.2);
        LatLng[] utilizatori = new LatLng[20];

        LatLng lastLocationPlace = sydney;
        ArrayList<String> fnume = new ArrayList<String>();
        int j = 0;
        String[] Lati = new String[50];
        String[] Longi = new String[50];
        String[] nameUser = new String[50];

        int nrUtil = 0;
        int k=0;
        for (int i = 0; i < MainActivity.Fcoords.length; i += 3) {
            nameUser[k] = MainActivity.Fcoords[i];
            System.out.println(nameUser[i] + " name ");
            Lati[k] = MainActivity.Fcoords[i + 1];
            System.out.println(Lati[i + 1] + " latitudine ");
            Longi[k] = MainActivity.Fcoords[i + 2];
            System.out.println(Longi[i + 2] + "longitudine");
            nrUtil++;
            k=k+1;
        }

        for (int s = 0; s < nrUtil; s++)
        {
          //  System.out.println(Lati[s]+" "+Longi[s]);
            double val1 = Double.parseDouble(Lati[s]);
            double val2 = Double.parseDouble(Longi[s]);
            utilizatori[s]=new LatLng(val1,val2);
            googleMap.addMarker(new MarkerOptions()
                    .position(utilizatori[s])
                    .title(nameUser[s]));
        }

        try {

                Location location = MyApplication.singleton.getMyLocation();

                    LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
                    MarkerOptions markerOptions =new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Lat:"+location.getLatitude()+"Lon:"+location.getLongitude());
                    googleMap.addMarker(markerOptions);
                    lastLocationPlace=latLng;

            } catch (NullPointerException e) {
                System.out.println("e null");
            }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastLocationPlace,17);
            googleMap.moveCamera(cameraUpdate);


    }


}





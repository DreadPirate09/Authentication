package com.example.list;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final int PERMISSION_FINE_LOCATION = 99;
    private Handler mHandler = new Handler();

    TextView tv_adress, uniqueCode;
    Button btn_newWayPoint, btn_showMap, btn_listCar;
    Button addAFriend;
    public static String[] Fcoords;
    //curent location

    Location currentLocation;
    //list of saved locations
    List<Location> savedLocation;
    LocationCallback locationCallback;
    private int once = 0;
    //set all proprietes of LocaionRequest

    LocationRequest locationRequest = LocationRequest.create();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("UseSwitchCompatOrMaterialCode")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // var tracking on/off

        //Location request

        //give each UI variabe a value

        tv_adress = findViewById(R.id.tv_address);
        uniqueCode = findViewById(R.id.uniqueCode);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_listCar = findViewById(R.id.btn_listCar);
        addAFriend = findViewById(R.id.addAFriend);

        locationRequest.setInterval(7000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        addAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddFriends.class);
                startActivity(i);
            }
        });

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save location
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };

        uniqueCode.setText("No code generated");


        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFriendsCoordinates(); // getFriendsCoordinates primeste coordonatele tuturor prieteniilor ca un string
                openDialog("Coordonatele au fost actualizate");
                String vars2 = getFriendsCoordinates();

                vars2 = vars2.replaceAll("]", "");
                vars2 = vars2.replaceAll("\\[", "");
                vars2 = vars2.replaceAll("\"", "");
                String[] vars22 = vars2.split(",");

                Fcoords=vars22;
                LatLng latLng = new LatLng(45.9, 21.9);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                String unicode = randomString();
                uniqueCode.setText(unicode);
                String unikey = uniqueCode.getText().toString();
                uniqueCode.setText("Codul dumneavoastra :" + unicode);
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocation = myApplication.getMyLocations();//savedLocation =global listp

                doPostRequestNameCoords(String.valueOf(currentLocation.getLatitude() + "," + currentLocation.getLongitude()), unikey);
                savedLocation.add(currentLocation); //adaugam o noua locatie in lista
            }
        });


        btn_listCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ListOfCars.class);
                startActivity(i);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        startLocationUpdates(); //live location
        updateGps();
        // mToastRunnable.run();


    }// end onCreate method


    private void addUtil(double v, double v1, @NonNull GoogleMap googleMap) {
        LatLng sydney = new LatLng(v, v1);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
    }

    public final Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            startLocationUpdates(); //live location
            updateGps();

            MyApplication myApplication = (MyApplication) getApplicationContext();
            savedLocation = myApplication.getMyLocations();//savedLocation =global list
            savedLocation.add(currentLocation);//adaugam o noua locatie in lista
            MyApplication.singleton.setMyLocations(savedLocation);
            mHandler.postDelayed(this, 5000);
            //Coordonates(currentLocation);

        }
    };

    private void startLocationUpdates() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            updateGps();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGps();
                } else {
                    Toast.makeText(this, "this app requires permission to be garanted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void updateGps() {
        //Primeste permisiune de la user pentru a fi accesta serviciul de GPS al dispozitivului
        // Face update cu locatia la interfata utilizatorului
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //in cazul in care primim permisiunea
                    updateUIValues(location);
                    currentLocation = location;
                }
            });
        } else {
            //In cazul in care nu primi permisiunea
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //verificam versiunea androidului
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private void updateUIValues(Location location) {
        //update all of the text view objects with a new location

        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_adress.setText("Locatia curenta : "+addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            tv_adress.setText("unable adress");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        //System.out.println(generatedString);

        return generatedString;
    }

    private String doPostRequestNameCoords(String coordinates, String key) {
        Log.d("OKHTTP3", "Post function called to send the user coordinates");
        String url = GlobalVars.url + "/postCoordinates";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("name", Login.userName);
            actualData.put("coordinates", coordinates);
            actualData.put("uniqueCode", key);
            Log.d("OKHTTP3", "add the message");
        } catch (JSONException e) {
//            openDialog("No image selected");
            e.printStackTrace();
            Log.d("OKHTTP3", "JSON excetion");
        }
        RequestBody body = RequestBody.create(JSON, actualData.toString());
        Request newReq = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(newReq).execute();
            Log.d("OKHTTP3", "Request Done, got the response.");
            assert response.body() != null;
            return response.body().string();

        } catch (IOException e) {
            Log.d("OKHTTP3", "Exception while doing request.");
            e.printStackTrace();
            return "ASD";
        }
    }

    private String getFriendsCoordinates() {
        Log.d("OKHTTP3", "Trimiterer cod prieten");
        String url = GlobalVars.url + "/getAllFriendsCoordinates";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("user", Login.userName);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("OKHTTP3", "JSON excetion");
        }
        RequestBody body = RequestBody.create(JSON, actualData.toString());
        Request newReq = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(newReq).execute();
            Log.d("OKHTTP3", "Request efectuat");
            assert response.body() != null;
            return response.body().string();

        } catch (IOException e) {
            Log.d("OKHTTP3", "Exception while doing request.");
            e.printStackTrace();
            return "Failed";
        }
    }

    public void openDialog(String msg) {
        MessageDialog dialog = new MessageDialog(msg);
        dialog.show(getSupportFragmentManager(), msg);
    }



    }

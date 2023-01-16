package com.example.list;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity {


    TextView tv_adress;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("UseSwitchCompatOrMaterialCode")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_adress = findViewById(R.id.tv_address);

    }

    public void openDialog(String msg) {
        MessageDialog dialog = new MessageDialog(msg);
        dialog.show(getSupportFragmentManager(), msg);
    }



    }

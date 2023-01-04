package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login extends AppCompatActivity {

    Button login,register;
    TextView name, password;
    public static String userName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        login = findViewById(R.id.login_button);
        name = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nameString = name.getEditableText().toString();
                String passwordString = password.getEditableText().toString();
                String res = doPostRequestNamePassword(nameString,passwordString);

                JSONObject obj = null;
                String textRecived = null;
                try {
                    obj = new JSONObject(res);
                    textRecived =(String) obj.get("raspuns");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(textRecived.equals("access granted")){
                    Intent i = new Intent(Login.this,MainActivity.class);
                    userName = name.getEditableText().toString();
                    startActivity(i);
                }else{
                    openDialog("Wrong password");
                }

                System.out.println(res);
            }
        });
    }

    private String doPostRequestNamePassword(String name, String password) {
        Log.d("OKHTTP3","Trimiterer nume si parola catre sever");
        String url = GlobalVars.url+"/login";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("name",name);
            actualData.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("OKHTTP3","JSON excetion");
        }
        RequestBody body = RequestBody.create(JSON,actualData.toString());
        Request newReq = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(newReq).execute();
            Log.d("OKHTTP3", "Request efectuat");
            assert response.body() != null;
            return response.body().string();

        }catch (IOException e)
        {
            Log.d("OKHTTP3", "Exception while doing request.");
            e.printStackTrace();
            return "Failed";
        }
    }
    public void openDialog(String msg){
        MessageDialog dialog = new MessageDialog(msg);
        dialog.show(getSupportFragmentManager(), msg);
    }

}
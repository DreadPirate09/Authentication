package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class Register extends AppCompatActivity {

    Button register;
    TextView password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.registerBtn);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = email.getEditableText().toString();
                String passwordString = password.getEditableText().toString();
                String res = doPostRequestNamePassword(nameString,passwordString);
                openDialog("Cont creat cu success");
                JSONObject obj = null;
                String textRecived = null;
                try {
                    obj = new JSONObject(res);
                    textRecived =(String) obj.get("raspuns");
                    Login.userName = nameString;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(res);
            }
        });
    }

    private String doPostRequestNamePassword(String email, String password) {
        Log.d("OKHTTP3","Post function called");
        String url = GlobalVars.url+"/register";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("email",email);
            actualData.put("password",password);
            Log.d("OKHTTP3","add the message");
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
            Log.d("OKHTTP3", "Request Done, got the response.");
            assert response.body() != null;
            return response.body().string();

        }catch (IOException e)
        {
            Log.d("OKHTTP3", "Exception while doing request.");
            e.printStackTrace();
            return "ASD";
        }
    }
    public void openDialog(String msg){
        MessageDialog dialog = new MessageDialog(msg);
        dialog.show(getSupportFragmentManager(), msg);
    }

}
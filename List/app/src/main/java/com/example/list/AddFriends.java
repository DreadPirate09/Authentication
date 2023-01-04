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

public class AddFriends extends AppCompatActivity {
    Button add;
    TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        add = this.findViewById(R.id.add);
        code = this.findViewById(R.id.code);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod = code.getEditableText().toString();
                openDialog(addFriend(cod));
            }
        });
    }

    private String addFriend(String code) {
        Log.d("OKHTTP3","Trimiterer cod prieten");
        String url = GlobalVars.url+"/addFriend";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("code",code);
            actualData.put("user",Login.userName);
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
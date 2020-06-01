package com.example.hp.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity {

    private String email;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        Button button=findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                put();
            }
        });
    }
    public void put()
    {
        editText=findViewById(R.id.email);
        email=editText.getText().toString();
        System.out.print("======="+email);
        if(email==null)return;;
        JSONObject param=new JSONObject();
        try {
            param.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json=param.toString();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        String uu=Data1.url+"/user/findPassword?email="+email;
        Request request = new Request.Builder()
                .url(uu)
                .get()
                .build();
        System.out.print("updateUserNameInter\n");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
                System.out.print("--------fail"+err);
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  rtn= response.body().string();
                System.out.println("==========="+rtn);
            }

        } );
    }

}

package com.example.hp.myapplication;

import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttp {


    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).addHeader("id",Data1.ID).addHeader("token",Data1.Token).get().build();
        client.newCall(request).enqueue(callback);
    }

}

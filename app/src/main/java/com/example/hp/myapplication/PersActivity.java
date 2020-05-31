package com.example.hp.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONArray;
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

public class PersActivity extends AppCompatActivity {

    Message message = Message.obtain();
    private TextView edit1;
    private View button1;
    private TextView edit2;
    private View button2;
    private TextView edit3;
    private View button3;
    private TextView edit4;
    private View button4;
    String tel,name,sex,email;
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                edit1.setText(name);
                edit2.setText(sex);
                edit3.setText(email);
                edit4.setText(tel);
            }
        }
    };

    public void init(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(Data1.url+"/user/getUserInfo")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  rtn= response.body().string();
                System.out.print("个人数据---------"+rtn+"\n");
                try {
                    JSONObject value = new JSONObject(rtn);
                    tel=value.getString("tel");
                    name=value.getString("name");
                    email=value.getString("email");
                    sex=value.getString("sex");
                    if(sex.equals("1"))sex="男";
                    else sex="女";
                    System.out.print("----------"+name);

                    message.what = 0x11;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } );

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers);
        edit1=findViewById(R.id.textView11);
        button1=findViewById(R.id.image11);
        edit2=findViewById(R.id.textView22);
        button2=findViewById(R.id.image12);
        edit3=findViewById(R.id.textView33);
        button3=findViewById(R.id.image13);
        edit4=findViewById(R.id.textView44);
        button4=findViewById(R.id.image14);
        init();
    }

    public void updateEmail(String email)throws JSONException
    {
        JSONObject param=new JSONObject();
        param.put("email",email);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"/user/getUserInfo")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(body )
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  rtn= response.body().string();
                System.out.println("==========="+rtn);
            }

        } );
    }

    public void updateUserName(String name)throws JSONException
    {
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .get()
//                .url(Data1.url+"/user/getUserInfo")
//                .addHeader("id",Data1.ID)
//                .addHeader("token",Data1.Token)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String err = e.getMessage().toString();
//            }
//            //请求成功执行的方法
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String  rtn= response.body().string();
//                System.out.print("个人数据---------"+rtn+"\n");
//
//            }
//
//        } );

        JSONObject param=new JSONObject();
        param.put("userName",name);
        String json=param.toString();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"/user/updateUserName")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(body)
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
    public void updateTel(String tel)throws JSONException
    {
        JSONObject param=new JSONObject();
        param.put("tel",tel);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"/user/updateTel")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(body )
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  rtn= response.body().string();
                System.out.println("==========="+rtn);
            }

        } );
    }



    public void r1(final View view) {


        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(view==button1){
                    try {
                        edit1.setText(inputServer.getText().toString());
                        updateUserName(edit1.getText().toString());
                        System.out.print("uodatename--------------"+edit1.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(view==button2) {
                    edit2.setText(inputServer.getText().toString());
                }
                else if(view==button3){
                    edit3.setText(inputServer.getText().toString());
                    try {
                        updateEmail(edit3.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    edit4.setText(inputServer.getText().toString());
                    try {
                        updateTel(edit4.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.show();
        }

}

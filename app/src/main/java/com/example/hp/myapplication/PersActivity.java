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
    public void showAddDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog, null);
        final EditText oldpassword = (EditText) textEntryView.findViewById(R.id.editTextName);
        final EditText newpassword = (EditText)textEntryView.findViewById(R.id.editTextNum);
        final EditText newpassword1 = (EditText)textEntryView.findViewById(R.id.editTextNum1);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(PersActivity.this);

        String p3=oldpassword.getText().toString();

        ad1.setTitle("修改密码:");
        ad1.setIcon(R.drawable.edit);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String p1=newpassword.getText().toString();
                String p2=newpassword1.getText().toString();
                if(p1!=p2) {
                    View focusView = null;
                    newpassword1.setError("请输入相同的密码");
                    focusView = newpassword1;
                    focusView.requestFocus();
                    return;
                }
                System.out.print("updatep===========\n");
                JSONObject param=new JSONObject();
                try {
                    param.put("newPassword",newpassword.getText().toString());
                    param.put("oldPassword",oldpassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.print("============"+p1+"\n");
                String json=param.toString();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(Data1.url+"/user/updatePassword")
                        .addHeader("id",Data1.ID)
                        .addHeader("token",Data1.Token)
                        .post(body)
                        .build();
                System.out.print("updatePassword\n");
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
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers);
        edit1 = findViewById(R.id.textView11);
        button1 = findViewById(R.id.image11);
        edit2 = findViewById(R.id.textView22);
        edit3 = findViewById(R.id.textView33);
        edit4 = findViewById(R.id.textView44);
        button4 = findViewById(R.id.image14);
        init();

        Button button = findViewById(R.id.buttonw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
                    }
        });
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
                    //edit2.setText(inputServer.getText().toString());
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

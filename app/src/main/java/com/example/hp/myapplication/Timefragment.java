package com.example.hp.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class Timefragment extends Fragment
{
    Message message = Message.obtain();
    private String[] Data={"学习","打扫"};//相应的待办

    private String acctime=null;
    private String daytime=null;
    private String monthtime=null;
    private AlertDialog alertDialog1;
    @SuppressLint("ValidFragment")
    private TextView ac;
    private TextView day;
    private TextView month;

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ac.setText(acctime);
            }
            else if(msg.what== 2) {
                day.setText(daytime);
            }
            else if(msg.what==3) {
                month.setText(monthtime);
            }
        }
    };

    public void init()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(Data1.url+"/record/getAccRecord")
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
                System.out.print("time========="+rtn+"\n");
                try {
                    JSONObject json = new JSONObject(rtn);
                    int s=json.optInt("successCount");
                    acctime=String.valueOf(s);
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );
    }

    public void init1()
    {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        System.out.println("date----------"+ee);
        OkHttpClient client = new OkHttpClient();

        String uu=Data1.url+"/record/getDailyRecord?date="+ee;
        Request request = new Request.Builder()
                .get()
                .url(uu)
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
                System.out.print("time========="+rtn+"\n");
                try {
                    JSONObject json = new JSONObject(rtn);
                    int s=json.optInt("successCount");
                    acctime=String.valueOf(s);
                    message.what = 2;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );
    }
    public void init2()
    {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        System.out.println("moth----------"+ee);
        OkHttpClient client = new OkHttpClient();

        String uu=Data1.url+"/record/getMonthRecord?date="+ee;
        Request request = new Request.Builder()
                .get()
                .url(uu)
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
                System.out.print("time========="+rtn+"\n");
                try {
                    JSONObject json = new JSONObject(rtn);
                    int s=json.optInt("successCount");
                    acctime=String.valueOf(s);
                    message.what = 3;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        init1();
        init2();
    }

    public void showSingleAlertDialog(View view){
        int c=0;
        final String[] items = {"5分钟", "15分钟","30分钟","60分钟","90分钟","120分钟","150分钟","180分钟","210分钟","240分钟","270分钟","300分钟"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("快速锁屏请选择时长");
        final Intent intent = new Intent(getActivity(), LockActivity.class);
        intent.putExtra("timelong", 5);
        intent.putExtra("id",Data1.ID);
        intent.putExtra("token",Data1.Token);
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), items[i], Toast.LENGTH_SHORT).show();
                if(i==0) intent.putExtra("timelong", 5);
                else if(i==1) intent.putExtra("timelong", 15);
                else if(i==2) intent.putExtra("timelong", 30);
                else if(i==3)intent.putExtra("timelong", 60);
                else if(i==4)intent.putExtra("timelong", 90);
                else if(i==5)intent.putExtra("timelong", 120);
                else if(i==6)intent.putExtra("timelong", 150);
                else if(i==7)intent.putExtra("timelong", 180);
                else if(i==8)intent.putExtra("timelong", 210);
                else if(i==9)intent.putExtra("timelong", 240);
                else if(i==10)intent.putExtra("timelong", 270);
                else intent.putExtra("timelong", 300);

            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(intent);
                getActivity().finish();
                alertDialog1.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View messageLayout=inflater.inflate(R.layout.tim,container,false);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        TextView tClick = messageLayout.findViewById(R.id.textView18);
        tClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleAlertDialog(view);
            }
        });
        ac=messageLayout.findViewById(R.id.textView8);
        day=messageLayout.findViewById(R.id.textView6);
        month=messageLayout.findViewById(R.id.textView16);
        ac.setText(acctime);
        day.setText(daytime);
        month.setText(monthtime);

        return  messageLayout;
    }

}
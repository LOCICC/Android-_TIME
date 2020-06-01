package com.example.hp.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.myapplication.adapter.MyAdapter;
import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class showActivity extends AppCompatActivity implements MyAdapter.InnerItemOnclickListener,
        AdapterView.OnItemLongClickListener {
    private Toolbar toolbar;
    MyAdapter adapter;
    ListView listview;
    Message message = Message.obtain();
    //需要数据：待办集的待办名称和时长列表
    List<String>title= new ArrayList<>();
    List<Integer>Time= new ArrayList<>();
    List<String>time=new ArrayList<String>();
    List<Integer>todoid= new ArrayList<>();
    List<Integer>status=new ArrayList<>();
    boolean flag=false;
    //将数据封装成数据源
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private int setid;
    public void init1()
    {
        title.add("背英语");
        time.add("40分钟");
    }

    public void init()throws JSONException
    {
        JSONObject param=new JSONObject();

        param.put("userTodoSetId",setid);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(body)
                .url(Data1.url+"/userTodo/listById")
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
                System.out.print("todo数据---------"+rtn+"\n");
                try {
                    JSONArray jsonArray = new JSONArray(rtn);
                    int size=jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject value = jsonArray.getJSONObject(i);
                        //获取到title值
                        title.add(value.getString("name"));
                        System.out.print("name======="+value.getString("name")+"\n");
                        Time.add(value.optInt("time"));
                        Time.add(value.optInt("time"));
                        int t=value.optInt("time");
                        time.add(String.valueOf(t));
                        todoid.add(value.optInt("userTodoId"));
                        status.add(value.optInt("todoStatusId"));
                    }
                    for(int i=0;i<title.size();i++){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", title.get(i));
                        map.put("time", time.get(i)+"分钟");
                        map.put("begin", "开始");
                        System.out.println("???????"+status.get(i)+"\n");
                        map.put("status",status.get(i));
                        map.put("id",todoid.get(i));
                        list.add(map);
                    }
                    message.what = 0x11;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        toolbar = findViewById(R.id.toolbar);

        setid=getIntent().getIntExtra("todosetid",1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        String Message = intent.getStringExtra("Message");
        getSupportActionBar().setTitle(Message);

        initView();
        adapter = new MyAdapter(list, this);
        listview.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        adapter.setOnInnerItemOnClickListener(this);
        listview.setOnItemLongClickListener(this);
    }

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                //更新ui
                adapter.notifyDataSetChanged();
            }
        }
    };
    public void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(showActivity.this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(showActivity.this).inflate(
                R.layout.dialog1, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        dialog.setTitle("添加待办");
        // 初始化控件，注意这里是通过view.findViewById
        final EditText daiban = (EditText) view.findViewById(R.id.edt);
        final EditText duration = (EditText) view.findViewById(R.id.duration);
        duration.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 确定
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String name = daiban.getText().toString();
                String time = duration.getText().toString();
                if (name.length() == 0 || time.length() == 0) {
                    Toast.makeText(showActivity.this, "请输入待办名称和时间！", Toast.LENGTH_SHORT).show();
                } else {

                    //将待办名称name和时长time存入数据库

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", name);
                    map.put("time", time+"分钟");
                    map.put("begin", "开始");
                    map.put("status","1");
                    list.add(map);
                    try {
                        Create(time,name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(showActivity.this, "添加待办成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.add2:
                //添加待办
                dialog();
                break;
            default:
        }
        return true;
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listView);
    }


    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.cardBegin:
                Toast.makeText(showActivity.this, "开始计时" , Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    public void Delete (int itemNum)throws JSONException
    {
        JSONObject param=new JSONObject();

        param.put("userTodoId",todoid.get(itemNum));
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"/userTodo/delete")
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

                System.out.print("delete============"+rtn+"\n");
            }

        } );
    }

    public void Create (final String tim, final String name)throws JSONException
    {
        int t=0;
        for(int i=0;i<tim.length();i++) {
            char o=tim.charAt(i);
            if(o>='0'&&o<='9') {
                t=t*10;
                t=t+(o-'0');
            }
            else break;;
        }
        Time.add(t);
        JSONObject param=new JSONObject();

        param.put("userTodoSetId",setid);
        param.put("time",t);
        param.put("name",name);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"/userTodo/create")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(body )
                .build();
        final int finalT = t;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  rtn= response.body().string();
                System.out.print("create============"+rtn+"\n");
                status.add(1);
                title.add(name);
                time.add(tim);
                JSONObject value= null;
                try {
                    value = new JSONObject(rtn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                todoid.add(value.optInt("userTodoId"));


            }

        } );
    }
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                   long id) {
        final int itemNum = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(showActivity.this);
        builder.setTitle("提示");// 设置标题
        builder.setMessage("是否删除待办?");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        // 为对话框设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                list.remove(itemNum);
                title.remove(itemNum);
                Time.remove(itemNum);
                time.remove(itemNum);
                todoid.remove(itemNum);
                try {
                    Delete(itemNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(showActivity.this, "删除待办成功！", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();// 使用show()方法显示对话框
        return true;
    }
}

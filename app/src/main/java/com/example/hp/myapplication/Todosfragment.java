package com.example.hp.myapplication;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.myapplication.adapter.daibanjiAdapter;
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

public class Todosfragment extends Fragment implements daibanjiAdapter.InnerItemOnclickListener,
        OnItemLongClickListener {
    Message message = Message.obtain();
    daibanjiAdapter adapter;
    ListView listview;
    private Toolbar toolbar;
    //需要数据：待办集名称列表
    List<String>title=new ArrayList<>();
    List<Integer>todosetid=new ArrayList<>();
    boolean flag=false;
    //将数据封装成数据源
    List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();

    static Todofragment newInstance(String id,String token){
        Todofragment newFragment=new Todofragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        bundle.putString("Token", token);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View messageLayout=inflater.inflate(R.layout.todos,container,false);
        toolbar = (Toolbar)messageLayout.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        init();
    //    while(!flag);
        listview = (ListView) messageLayout.findViewById(R.id.listView);
        adapter = new daibanjiAdapter(list, getContext());
        listview.setAdapter(adapter);

        adapter.setOnInnerItemOnClickListener(this);
        listview.setOnItemLongClickListener(this);
        //while(sizes==0){}
        return  messageLayout;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void init1()
    {
        title.add("今日学习");
    }
    public void init()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(Data1.url+"/userTodoSet/list")
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
                Log.d("todos数据",rtn);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(rtn);
                    int size=jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject value = jsonArray.getJSONObject(i);
                        title.add(value.getString("name"));
                        todosetid.add(value.optInt("userTodoSetId"));
                    }
                    for(int i=0;i<title.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("title",title.get(i));
                        map.put("show",R.drawable.show);
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
    public void Create (String name)throws JSONException
    {
        title.add(name);
        JSONObject param=new JSONObject();
        param.put("name",name);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"//userTodoSet/create")
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
                System.out.print("create============"+rtn+"\n");

                JSONObject value = null;
                try {
                    value = new JSONObject(rtn);
                    System.out.print("id======="+value.getString("userTodoSetId")+"\n");
                    todosetid.add(value.optInt("userTodoSetId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } );
    }

    public void Delete (int itemNum)throws JSONException
    {
        JSONObject param=new JSONObject();

        param.put("userTodoSetId",todosetid.get(itemNum));
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Data1.url+"//userTodoSet/delete")
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
    private void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog2, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        dialog.setTitle("添加待办集");
        // 初始化控件，注意这里是通过view.findViewById
        final EditText edt = (EditText) view.findViewById(R.id.edt);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 确定按钮
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String str = edt.getText().toString();
                if(str.length() == 0) {
                    Toast.makeText(getActivity() , "请输入待办集名称！",Toast.LENGTH_SHORT).show();
                } else{

                    //将待办集名称存入数据库

                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("title",str);
                    map.put("show",R.drawable.show);

                    list.add(map);
                    try {
                        Create(str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity() , "添加待办集成功！" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        // 取消按钮
        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar2, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add2:
                //添加待办集
                dialog();
                break;
            default:
        }
        return true;
    }


    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.show:
                Intent intent = new Intent();
                intent.setClass(getActivity(),showActivity.class);
                intent.putExtra("Message",list.get(position).get("title").toString() );
                intent.putExtra("todosetid",todosetid.get(position));
                startActivity(intent);
                break;
            default:
                break;
        }

    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                   long id) {
        final int itemNum = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");// 设置标题
        builder.setMessage("是否删除待办集?");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        // 为对话框设置确定按钮
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                list.remove(itemNum);
                try {
                    Delete(itemNum);
                    title.remove(itemNum);
                    todosetid.remove(itemNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "删除待办集成功！", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();// 使用show()方法显示对话框
        return true;
    }

}




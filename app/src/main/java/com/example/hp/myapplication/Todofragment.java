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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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

import javax.crypto.Mac;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Todofragment extends Fragment implements MyAdapter.InnerItemOnclickListener,
        OnItemLongClickListener {
    Message message = Message.obtain();
    private Todofragment m = null;
    private popWinShare popwindows;
    private Toolbar toolbar;
    View messageLayout;
    LoadProgressDialog loadProgressDialog;
    MyAdapter adapter;
    ListView listview;
    boolean flag=false;
    //需要数据：待办名称及时长
    //测试数据
    int size=0;
    List<String>title= new ArrayList<>();
    List<Integer>Time= new ArrayList<>();
    List<String>time=new ArrayList<String>();
    List<Integer>todoid= new ArrayList<>();
    List<Integer>status=new ArrayList<>();
    //将数据封装成数据源
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    public void init1()
    {
        title.add("背单词");
        title.add("做数学");
        title.add("画画");
        status.add(1);
        status.add(1);
        status.add(1);
        time.add("40");
        time.add("50");
        time.add("2");
        for(int i=0;i<title.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", title.get(i));
            map.put("time", time.get(i)+"分钟");
            map.put("begin", "开始");
            map.put("status",status.get(i));
            list.add(map);
        }
    }


    public void init()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(Data1.url+"/userTodo/listByUserId")
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
                    size=jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject value = jsonArray.getJSONObject(i);
                        //获取到title值
                        title.add(value.getString("name"));
                        System.out.print("name======="+value.getString("name")+"\n");
                        Time.add(value.optInt("time"));
                        int t=value.optInt("time");
                        time.add(String.valueOf(t));
                        todoid.add(value.optInt("userTodoId"));
                        JSONObject value1 =value.getJSONObject("todoStatus");
                        status.add(value1.optInt("todoStatusId"));
                    }
                    flag=true;
                    loadProgressDialog.dismiss();

                    for(int i=0;i<title.size();i++){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", title.get(i));
                        map.put("time", time.get(i)+"分钟");
                        map.put("begin", "开始");
                        map.put("status",status.get(i));
                        System.out.println("!!!!!!!!!"+title.get(i)+"!!!!!!!!"+status.get(i)+"\n");
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messageLayout=inflater.inflate(R.layout.todo,container,false);
        flag=false;
        m=this;
        loadProgressDialog=new LoadProgressDialog(getActivity(),"加载中……");
        loadProgressDialog.show();
       // loadProgressDialog.setMessage("加载数据中……");
        listview = messageLayout.findViewById(R.id.listView);
        toolbar = messageLayout.findViewById(R.id.toolbar);
        adapter = new MyAdapter(list,getContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        listview.setAdapter(adapter);
        init();
//        while(!flag);
//        init1();

        adapter.notifyDataSetChanged();
        adapter.setOnInnerItemOnClickListener(m);
        listview.setOnItemLongClickListener(m);
        return  messageLayout;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void dialog() {

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
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
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String name = daiban.getText().toString();
                String time = duration.getText().toString();
                if (name.length() == 0 || time.length() == 0) {
                    Toast.makeText(getActivity(), "请输入待办名称和时间！", Toast.LENGTH_SHORT).show();
                } else {
                    //将待办名称name和时长time存入
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
                    Toast.makeText(getActivity(), "添加待办成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

        });
        // 取消
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
        inflater.inflate(R.menu.toolbar1, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.daka:
                popwindows = new popWinShare(getActivity());
                popwindows.showPopupWindow(toolbar);
                break;
            case R.id.add:
                dialog();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        int id=v.getId();
        if(id==R.id.cardBegin)
        {
            Toast.makeText(getActivity(), "开始计时" , Toast.LENGTH_SHORT).show();
            String tim=list.get(position).get("time").toString();
            int time=0;
            String todo=todoid.get(position).toString();
            for(int i=0;i<tim.length();i++) {
                char o=tim.charAt(i);
                if(o>='0'&&o<='9') {
                    time=time*10;
                    time=time+(o-'0');
                }
                else break;;
            }
            System.out.print("============"+time);
            final Intent intent = new Intent(getActivity(), LockActivity.class);
            intent.putExtra("timelong", time);
            intent.putExtra("usertodoid",todo);
            startActivity(intent);
            getActivity().finish();
        }

    }

    public void Delete (int itemNum)throws JSONException
    {
        JSONObject param=new JSONObject();

        param.put("userTodoId",todoid.get(itemNum));

        title.remove(itemNum);
        Time.remove(itemNum);
        time.remove(itemNum);
        todoid.remove(itemNum);
        status.remove(itemNum);

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

        param.put("userTodoSetId","0");
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");// 设置标题
        builder.setMessage("是否删除待办?");// 为对话框设置内容
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "删除待办成功！", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();// 使用show()方法显示对话框
        return true;
    }

}




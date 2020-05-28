package com.example.hp.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Todofragment extends Fragment implements MyAdapter.InnerItemOnclickListener,
        OnItemLongClickListener {

    private popWinShare popwindows;
    private Toolbar toolbar;
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
    //将数据封装成数据源
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    static Todofragment newInstance(String id,String token){
        Todofragment newFragment=new Todofragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        bundle.putString("Token", token);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    public void init1()
    {
        title.add("背单词");
        title.add("做数学");
        title.add("画画");
        time.add("40");
        time.add("50");
        time.add("2");
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
                        time.add(value.getString("time"));
                        todoid.add(value.optInt("userTodoId"));
                     //   status[i]=value.getString("status");
                    }
                    flag=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } );
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View messageLayout=inflater.inflate(R.layout.todo,container,false);
//        init();
//        while(!flag){}
        init1();
        listview = messageLayout.findViewById(R.id.listView);
        toolbar = messageLayout.findViewById(R.id.toolbar);
        adapter = new MyAdapter(list,getContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        listview.setAdapter(adapter);
        for(int i=0;i<title.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", title.get(i));
            map.put("time", time.get(i)+"分钟");
            map.put("begin", "开始");
            list.add(map);
        }
        adapter.notifyDataSetChanged();
        adapter.setOnInnerItemOnClickListener(this);
        listview.setOnItemLongClickListener(this);

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
            startActivity(intent);
            getActivity().finish();
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

    public void Create (final String tim, String name)throws JSONException
    {
        JSONObject param=new JSONObject();

        param.put("userTodoSetId",0);
        param.put("time",tim);
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
                try {
                    JSONArray jsonArray = new JSONArray(rtn);
                    int size=jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject value = jsonArray.getJSONObject(i);
                        //获取到title值
                        System.out.print("name======="+value.getString("name")+"\n");
                        title.add(value.getString("name"));
                        todoid.add(value.optInt("userTodoId"));
                        time.add(value.getString("time"));
                        Time.add(value.optInt("time"));
                        //   status[i]=value.getString("status");
                    }
                    flag=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
//                title.remove(itemNum);
//                Time.remove(itemNum);
//                time.remove(itemNum);
//                todoid.remove(itemNum);
//                try {
//                    Delete(itemNum);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(getActivity(), "删除待办成功！", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();// 使用show()方法显示对话框
        return true;
    }

}




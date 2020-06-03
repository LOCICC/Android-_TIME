package com.example.hp.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.hp.myapplication.adapter.MExpandableListAdapter;
import com.example.hp.myapplication.alldata.Data1;
import com.example.hp.myapplication.entity.Group;
import com.example.hp.myapplication.entity.Item;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.util.concurrent.TimeUnit;

public class TeamActivity extends AppCompatActivity {
    private ArrayList<Item> todoList = new ArrayList<Item>();
    private ArrayList<Group> gData = new ArrayList<Group>();
    private ArrayList<Group> gData2 = null;
    private ArrayList<ArrayList<Item>> iData = new ArrayList<ArrayList<Item>>();
    private ArrayList<ArrayList<Item>> iData2 = null;
    private ArrayList<Item> lData = null;
    private ArrayList<Item> lData2 = null;
    private Context mContext = this;
    private ExpandableListView exlist1;
    private ExpandableListView exlist2;
    private MExpandableListAdapter myAdapter1 = null;
    private MExpandableListAdapter myAdapter2 = null;


    String teamName = "";
    int leaderId = 0;
    int teamId = 0;

    String TAG = "test-team";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.team, menu);
        if (leaderId == Integer.parseInt(Data1.ID)) {
            menu.findItem(R.id.quit_item).setVisible(false);
        }
        else {
            menu.findItem(R.id.invite_item).setVisible(false);
            menu.findItem(R.id.break_item).setVisible(false);
            menu.findItem(R.id.out_item).setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //getItemId()判断点击的是哪个菜单项
        switch (item.getItemId()) {
            case R.id.add_todo_item:
                Toast.makeText(this, "You clicked 添加代办",Toast.LENGTH_SHORT).show();
                showDialog("todo");
                break;
            case R.id.add_todolist_item:
                Toast.makeText(this, "You clicked 添加代办集",Toast.LENGTH_SHORT).show();
                showDialog("todolist");
                break;
            case R.id.quit_item:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("确认");
                dialog.setMessage("是否确定退出？");
                // 对话框弹出后点击或按返回键不消失
                dialog.setCancelable(false);
                // 设定确定按钮的点击事件
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        JSONObject param = new JSONObject();
                        try {
                            param.put("teamId", teamId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String json = param.toString();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                                .build();
                        RequestBody requestBody = RequestBody.create(JSON,json);
                        Request request = new Request.Builder()
                                .url(Data1.url + "/team/quitTeam")
                                .addHeader("id",Data1.ID)
                                .addHeader("token",Data1.Token)
                                .post(requestBody)
                                .build();

                        Call task = client.newCall(request);
                        task.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call,IOException e) {
                                String error = e.getMessage().toString();
//                                Log.d(TAG, "error -> " + error);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                int code = response.code();
//                                Log.d(TAG, "code -> " + code);
                                if (code == HttpURLConnection.HTTP_OK) {
                                    ResponseBody body = response.body();
                                    if (body != null) {

                                        Log.d(TAG, "result -> " + body.string());
                                    }
                                }
                            }
                        });
                        dialog.dismiss();
                        finish();
                    }
                });
                // 设定取消按钮的点击事件
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.break_item:
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("确认");
                dialog.setMessage("是否确定解散？");
                // 对话框弹出后点击或按返回键不消失
                dialog.setCancelable(false);
                // 设定确定按钮的点击事件
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        JSONObject param = new JSONObject();
                        try {
                            param.put("teamId", teamId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String json = param.toString();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                                .build();
                        RequestBody requestBody = RequestBody.create(JSON,json);
                        Request request = new Request.Builder()
                                .url(Data1.url + "/team/deleteTeam")
                                .addHeader("id",Data1.ID)
                                .addHeader("token",Data1.Token)
                                .post(requestBody)
                                .build();

                        Call task = client.newCall(request);
                        task.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call,  IOException e) {
                                String error = e.getMessage().toString();
//                                Log.d(TAG, "error -> " + error);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                int code = response.code();
//                                Log.d(TAG, "code -> " + code);
                                if (code == HttpURLConnection.HTTP_OK) {
                                    ResponseBody body = response.body();
                                    if (body != null) {

                                        Log.d(TAG, "result -> " + body.string());
                                    }
                                }
                            }
                        });
                        dialog.dismiss();
                        finish();
                    }
                });
                // 设定取消按钮的点击事件
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.invite_item:
                showDialog("invite");
                break;
            case R.id.out_item:
                showDialog("out");
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        System.out.println("dddddddddddddddd\n");
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        teamName = intent.getStringExtra("param1");
        leaderId = intent.getIntExtra("param2", 0);
        teamId = intent.getIntExtra("param3", 0);


        getTodoList();
        long beginTime = System.currentTimeMillis();
        while (true) {
            long nowTime = System.currentTimeMillis();

            if (!iData.isEmpty()) {
                initView();
                break;
            }
            else if (nowTime-beginTime > 10 * 1000) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提醒");
                dialog.setMessage("连接超时");
                // 对话框弹出后点击或按返回键不消失
                dialog.setCancelable(false);
                // 设定确定按钮的点击事件
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            }
        }

    }

    private void initView() {
        exlist1 = findViewById(R.id.expandlist_team);
//        exlist2 = findViewById(R.id.expandlist_team_todo);

        Log.d(TAG, "G ->" + gData.size() + ", I -> " + iData.size());
        myAdapter1 = new MExpandableListAdapter(gData, iData, mContext, leaderId, Integer.parseInt(Data1.ID),TeamActivity.this);
//        myAdapter2 = new MExpandableListAdapter(gData2, iData2, mContext);
        exlist1.setAdapter(myAdapter1);
//        exlist2.setAdapter(myAdapter2);
        exlist1.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "You clicked " + iData.get(groupPosition).get(childPosition).getiName(),
                        Toast.LENGTH_SHORT).show();
                if (iData.get(groupPosition).get(childPosition).getiName().equals("新建")) {


                    final android.app.AlertDialog dialog;
                    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_create_team_todo, null);
                    dialog = new android.app.AlertDialog.Builder(mContext).setView(view).create();
                    Button btn_confirm = view.findViewById(R.id.dialog_btn_confirm);
                    final EditText todo_name = view.findViewById(R.id.dialog_edit_team_todo_name);
                    final EditText todo_time = view.findViewById(R.id.dialog_edit_team_todo_time);
                    ImageButton btn_close = view.findViewById(R.id.dialog_btn_close);
                    Button btn_cancel = view.findViewById(R.id.dialog_btn_cancel);

                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog progressDialog = new ProgressDialog(mContext);

                            progressDialog.setTitle("This is ProgressDialog");
                            progressDialog.setMessage("请稍后...");
                            // 数据加载完成后，调用ProgressDialog的dismiss()方法关闭对话框
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            //to do
                            String todoName = todo_name.getText().toString();
                            String todoTime = todo_time.getText().toString();
                            JSONObject param = new JSONObject();
                            Log.d(TAG, "TEAMID ->" + teamId);
                            try {
                                param.put("teamId", teamId);
                                param.put("name", todoName);
                                param.put("time", todoTime);
                                param.put("teamTodoSetId", gData.get(groupPosition).getTodoSetId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String json = param.toString();

                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            OkHttpClient client = new OkHttpClient.Builder()
                                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                                    .build();
                            RequestBody requestBody = RequestBody.create(JSON,json) ;
                            Request request = new Request.Builder()
                                    .url(Data1.url + "/teamTodo/create")
                                    .addHeader("id",Data1.ID)
                                    .addHeader("token",Data1.Token)
                                    .post(requestBody)
                                    .build();

                            Call task = client.newCall(request);
                            task.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    String error = e.getMessage().toString();
                                    Log.d(TAG, "error -> " + error);
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    int code = response.code();
                                    progressDialog.dismiss();
                                    Log.d(TAG, "code -> " + code);
                                    if (code == HttpURLConnection.HTTP_OK) {
                                        ResponseBody body = response.body();
                                        if (body != null) {
                                            String message = body.string();
                                            Log.d(TAG, "result -> " + message);
                                            if (message.equals("create-fail")) {
                                                Log.d(TAG, "创建失败");
                                            } else {
                                                Log.d(TAG, "创建成功");
                                            }
                                        }
                                    }
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //to do
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    dialog.setCancelable(false);
                }

                return true;
            }
        });

    }

    private void getTodoList() {
        JSONObject param = new JSONObject();
        Log.d(TAG, "TEAMID ->" + teamId);
        try {
            param.put("teamId", teamId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = param.toString();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(Data1.url + "/teamTodo/listByTeamId")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(requestBody)
                .build();

        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.getMessage().toString();
                Log.d(TAG, "error -> " + error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.d(TAG, "code -> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {

//                        Log.d(TAG, "result -> " + body.string());
                        parseJsonOftodoList(response);
                    }
                }
                getTodoSetList();
            }
        });

    }
    private void getTodoSetList() {
        JSONObject param = new JSONObject();
        Log.d(TAG, "TEAMID ->" + teamId);
        try {
            param.put("teamId", teamId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = param.toString();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url( Data1.url+ "/teamTodoSet/list")
                .addHeader("id",Data1.ID)
                .addHeader("token",Data1.Token)
                .post(requestBody)
                .build();

        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure( Call call, IOException e) {
                String error = e.getMessage().toString();
                Log.d(TAG, "error -> " + error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.d(TAG, "code -> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {

//                        Log.d(TAG, "result -> " + body.string());
                        parseJsonOftodoSetList(response);
                    }
                }
            }
        });

    }

    private void parseJsonOftodoList(Response response) throws IOException {
        String responseData=response.body().string();
        Log.d(TAG, "json --> " + responseData);

        try{
            JSONArray json = new JSONArray(responseData);
            if (json.length() != 0) {
                for (int i = 0; i < json.length(); i++) {
                    String name = json.getJSONObject(i).getString("name");
                    int time = json.getJSONObject(i).getInt("time");
                    int todoId = json.getJSONObject(i).getInt("teamTodoId");
                    int todoSetId = json.getJSONObject(i).getInt("teamTodoSetId");

                    Item todo = new Item(time, name, todoId, todoSetId, teamId);
                    todoList.add(todo);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonOftodoSetList(Response response) throws IOException {
        String responseData=response.body().string();

        Log.d(TAG, "json --> " + responseData);
        if (!todoList.isEmpty()) {
            try {
                JSONArray json = new JSONArray(responseData);

                for (int i = 0; i < json.length(); i++) {
                    lData = new ArrayList<Item>();
                    String name = json.getJSONObject(i).getString("name");
                    int todoSetId = json.getJSONObject(i).getInt("teamTodoSetId");
                    Group todoSet = new Group(name);
                    todoSet.setTodoSetId(todoSetId);
                    gData.add(todoSet);
                    for (Item item : todoList) {
                        Log.d(TAG, "todolist->" + todoList.size());
                        if (item.getTodoSetId() == todoSetId) {
                            lData.add(item);
                        }
                    }
                    lData.add(new Item("新建"));
                    iData.add(lData);
                }
                Log.d(TAG, "I ->" + iData.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            lData = new ArrayList<Item>();
            lData.add(new Item("新建"));
            iData.add(lData);
        }
    }

    private void showDialog(String type) {
        View view;
        final android.app.AlertDialog dialog;
        if (type.equals("todo")) {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_create_team_todo, null);
            dialog = new android.app.AlertDialog.Builder(this).setView(view).create();
            Button btn_confirm =  view.findViewById(R.id.dialog_btn_confirm);
            final EditText todo_name = view.findViewById(R.id.dialog_edit_team_todo_name);
            final EditText todo_time = view.findViewById(R.id.dialog_edit_team_todo_time);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("This is ProgressDialog");
                    progressDialog.setMessage("请稍后...");
                    // 数据加载完成后，调用ProgressDialog的dismiss()方法关闭对话框
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //to do
                    String todoName = todo_name.getText().toString();
                    String todoTime = todo_time.getText().toString();
                    JSONObject param = new JSONObject();
                    Log.d(TAG, "TEAMID ->" + teamId);
                    try {
                        param.put("teamId", teamId);
                        param.put("name", todoName);
                        param.put("time", todoTime);
                        param.put("teamTodoSetId", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = param.toString();

                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
                            .url(Data1.url + "/teamTodo/create")
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .post(requestBody)
                            .build();

                    Call task = client.newCall(request);
                    task.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call,  IOException e) {
                            String error = e.getMessage().toString();
                            Log.d(TAG, "error -> " + error);
                        }

                        @Override
                        public void onResponse( Call call, Response response) throws IOException {
                            int code = response.code();
                            Log.d(TAG, "code -> " + code);
                            if (code == HttpURLConnection.HTTP_OK) {
                                ResponseBody body = response.body();
                                if (body != null) {

                                    Log.d(TAG, "result -> " + body.string());
                                    progressDialog.dismiss();
                                    if (body.string().equals("create-success")) {
                                        Log.d(TAG, "创建成功");
                                    }
                                    else {
                                        Log.d(TAG, "创建失败");
                                    }
                                }
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });
        }
        else if (type.equals("todolist")) {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_create_team_todolist, null);
            dialog = new android.app.AlertDialog.Builder(this).setView(view).create();
            Button btn_confirm =  view.findViewById(R.id.dialog_btn_confirm);
            final EditText todoset_name = view.findViewById(R.id.dialog_edit_team_todolist_name);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("This is ProgressDialog");
                    progressDialog.setMessage("请稍后...");
                    // 数据加载完成后，调用ProgressDialog的dismiss()方法关闭对话框
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //to do
                    String todoSetName = todoset_name.getText().toString();
                    JSONObject param = new JSONObject();
                    Log.d(TAG, "TEAMID ->" + teamId);
                    try {
                        param.put("teamId", teamId);
                        param.put("name", todoSetName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = param.toString();

                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
                            .url(Data1.url + "/teamTodoSet/create")
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .post(requestBody)
                            .build();

                    Call task = client.newCall(request);
                    task.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            String error = e.getMessage().toString();
                            Log.d(TAG, "error -> " + error);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int code = response.code();
                            Log.d(TAG, "code -> " + code);
                            if (code == HttpURLConnection.HTTP_OK) {
                                ResponseBody body = response.body();
                                String message = body.string();
                                if (body != null) {

                                    Log.d(TAG, "result -> " + message);
                                    progressDialog.dismiss();
                                    if (message.equals("create-success")) {
                                        Log.d(TAG, "创建成功");
                                    }
                                    else {
                                        Log.d(TAG, "创建失败");

                                    }
                                }
                            }
                        }
                    });
                    dialog.dismiss();

                }
            });

        }
        else if (type.equals("invite")) {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_team_invite, null);
            dialog = new android.app.AlertDialog.Builder(this).setView(view).create();

            Button btn_confirm =  view.findViewById(R.id.dialog_btn_confirm);
            final EditText email1 = view.findViewById(R.id.dialog_edit_team_invite);

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do
                    String email = email1.getText().toString();
                    JSONObject param = new JSONObject();
                    Log.d(TAG, "TEAMID ->" + teamId);
                    try {
                        param.put("teamId", teamId);
                        param.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = param.toString();

                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
                            .url(Data1.url + "/team/inviteMember")
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .post(requestBody)
                            .build();

                    Call task = client.newCall(request);
                    task.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            String error = e.getMessage().toString();
                            Log.d(TAG, "error -> " + error);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int code = response.code();
                            Log.d(TAG, "code -> " + code);
                            if (code == HttpURLConnection.HTTP_OK) {
                                ResponseBody body = response.body();
                                if (body != null) {

                                    Log.d(TAG, "result -> " + body.string());
                                }
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });
        }
        else {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_team_out, null);
            dialog = new android.app.AlertDialog.Builder(this).setView(view).create();
            Button btn_confirm =  view.findViewById(R.id.dialog_btn_confirm);
            final EditText email2 = view.findViewById(R.id.dialog_edit_team_out);

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do
                    String email = email2.getText().toString();
                    JSONObject param = new JSONObject();
                    try {
                        param.put("teamId", teamId);
                        param.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = param.toString();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .url(Data1.url + "/team/outMember")
                            .post(requestBody)
                            .build();

                    Call task = client.newCall(request);
                    task.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call,  IOException e) {
                            String error = e.getMessage().toString();
                            Log.d(TAG, "error -> " + error);
                        }

                        @Override
                        public void onResponse(Call call,  Response response) throws IOException {
                            int code = response.code();
                            Log.d(TAG, "code -> " + code);
                            if (code == HttpURLConnection.HTTP_OK) {
                                ResponseBody body = response.body();
                                if (body != null) {

                                    Log.d(TAG, "result -> " + body.string());
                                }
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });

        }
        ImageButton btn_close = view.findViewById(R.id.dialog_btn_close);
        Button btn_cancel = view.findViewById(R.id.dialog_btn_cancel);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCancelable(false);
    }
    public static void actionStart(Context context, String teamName, int leaderId, int teamId) {
        Intent intent = new Intent(context, TeamActivity.class);
        intent.putExtra("param1", teamName);
        intent.putExtra("param2", leaderId);
        intent.putExtra("param3", teamId);
        context.startActivity(intent);
    }
}

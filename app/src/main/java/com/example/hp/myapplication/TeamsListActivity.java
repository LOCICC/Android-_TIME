package com.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.adapter.SelectionAdapter;
import com.example.hp.myapplication.alldata.Data1;
import com.example.hp.myapplication.entity.Selection;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class TeamsListActivity extends AppCompatActivity {

    private List<Selection> selectionList = new ArrayList<>();
    private SelectionAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamslist);
        getTeamList();
    }

    private void getTeamList() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(Data1.url + "/team/getTeams")
                .addHeader("id", Data1.ID)
                .addHeader("token", Data1.Token)
                .get()
                .build();
        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        parseJsonWithJsonObject(response);
                    }
                }
            }
        });
        long beginTime = System.currentTimeMillis();
        while (true) {
            long nowTime = System.currentTimeMillis();
            if (!selectionList.isEmpty()) {
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
        ListView listView = (ListView) findViewById(R.id.team_list);
        adapter = new SelectionAdapter(TeamsListActivity.this, R.layout.selection_item, selectionList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selection selection = selectionList.get(position);
                Toast.makeText(TeamsListActivity.this, "You clicked " + selection.getName(),
                        Toast.LENGTH_SHORT).show();
                TeamActivity.actionStart(TeamsListActivity.this, selection.getName(), selection.getLeaderId(), selection.getTeamId());
            }
        });

    }


    private boolean parseJsonWithJsonObject(Response response) throws IOException {
        String responseData=response.body().string();

        try{
            JSONArray json = new JSONArray(responseData);
//            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < json.length(); i++) {
                String name = json.getJSONObject(i).getString("name");
                JSONObject leader = json.getJSONObject(i).getJSONObject("leader");
                int leaderId = leader.getInt("userId");
                int teamId = json.getJSONObject(i).getInt("teamId");
                Selection team = new Selection(name);
                team.setLeaderId(leaderId);
                team.setTeamId(teamId);
                selectionList.add(team);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    private void initSelections() {

        Selection team1 = new Selection("xxxx小组");
        selectionList.add(team1);
    }

    public void showDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_team, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        final TextView create = view.findViewById(R.id.dialog_selection_create_team);
        final TextView join = view.findViewById(R.id.dialog_selection_join_team);
        final EditText team_name = view.findViewById(R.id.dialog_edit_team);

        ImageButton btn_close = view.findViewById(R.id.dialog_btn_close);
        Button btn_confirm =  view.findViewById(R.id.dialog_btn_confirm);
        Button btn_cancel = view.findViewById(R.id.dialog_btn_cancel);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setBackgroundResource(R.drawable.textview);
                join.setBackgroundResource(R.drawable.textview2);
                create.setClickable(false);
                join.setClickable(true);
                team_name.setHint("请输入团队名称");
                team_name.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        create.setClickable(false);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setBackgroundResource(R.drawable.textview2);
                join.setBackgroundResource(R.drawable.textview);
                create.setClickable(true);
                join.setClickable(false);
                team_name.setInputType(InputType.TYPE_CLASS_NUMBER);
                team_name.setHint("请输入团队id");
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /**
         * confirm
         */
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do
                String teamName = team_name.getText().toString();
//                Selection team = new Selection(teamName);
//                selectionList.add(team);

                MediaType JSON = MediaType.parse("application/json;charse=utf-8");
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10000, TimeUnit.MILLISECONDS)
                        .build();

                Call task;
                if (team_name.getInputType() == InputType.TYPE_CLASS_TEXT) {
                    JSONObject param = new JSONObject();
                        try {
                            param.put("name", teamName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String json = param.toString();

                        RequestBody requestBody = RequestBody.create(JSON, json);

                        Request request = new Request.Builder()
                                .url(Data1.url + "/team/createTeam")
                                .addHeader("id", Data1.ID)
                                .addHeader("token", Data1.Token)
                                .post(requestBody)
                                .build();
                        task = client.newCall(request);

                }
                else {
                    JSONObject param = new JSONObject();
                    try {
                        param.put("teamId", teamName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = param.toString();
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
//                .url("http://10.0.2.2:9102/get/text")
                            .url(Data1.url+ "/team/joinTeam")
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .post(requestBody)
                            .build();
                    task = client.newCall(request);
                }

                task.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        String error = e.getMessage().toString();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        String  rtn= response.body().string();
                        System.out.print("create============"+rtn+"\n");
                        if (code == HttpURLConnection.HTTP_OK) {
                            ResponseBody body = response.body();
                            if (body != null) {
//                                if (parseJsonWithJsonObject(response)) {
//                                    initView();
//                                }

                            }
                        }
                    }
                });
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
    private static final String TAG = "test-teamlist";

}

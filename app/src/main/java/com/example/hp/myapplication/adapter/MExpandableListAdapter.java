package com.example.hp.myapplication.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.LockActivity;
import com.example.hp.myapplication.alldata.Data1;
import com.example.hp.myapplication.entity.Group;
import com.example.hp.myapplication.entity.Item;
import com.example.hp.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MExpandableListAdapter extends BaseExpandableListAdapter {

    String baseUrl = "http://5a774f3c501a.ngrok.io";
    private ArrayList<Group> gData;
    private ArrayList<ArrayList<Item>> iData;
    private Context mContext;
    private int leaderId;
    private int currUser;
    private Activity nowActivity;
    public MExpandableListAdapter(ArrayList<Group> gData, ArrayList<ArrayList<Item>> iData, Context mContext, int leaderId, int currUser,Activity now) {
        this.nowActivity=now;
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
        this.leaderId = leaderId;
        this.currUser = currUser;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if (convertView == null) {
            if (gData.get(groupPosition).getgName().equals("待办")) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.expandlist_group, parent, false);
                TextView group_name = (TextView) convertView.findViewById(R.id.group_name);
                group_name.setTextSize(18);
            }
            else {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.expandlist_group, parent, false);
            }
            groupHolder = new ViewHolderGroup();
            groupHolder.group_name = (TextView) convertView.findViewById(R.id.group_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.group_name.setText(gData.get(groupPosition).getgName());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (leaderId == currUser) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.expandlist_item, parent, false);

            ((TextView) convertView.findViewById(R.id.item_time)).setText(iData.get(groupPosition).get(childPosition).getiTime() + "分钟");
            ((TextView) convertView.findViewById(R.id.item_name)).setText(iData.get(groupPosition).get(childPosition).getiName());
            if (iData.get(groupPosition).get(childPosition).getiName().equals("新建")) {
                convertView.findViewById(R.id.btn_start).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.item_time).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);

            }
        }
        else {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.expandlist_item, parent, false);
            ((TextView) convertView.findViewById(R.id.item_time)).setText(iData.get(groupPosition).get(childPosition).getiTime() + "分钟");
            ((TextView) convertView.findViewById(R.id.item_name)).setText(iData.get(groupPosition).get(childPosition).getiName());
            if (iData.get(groupPosition).get(childPosition).getiName().equals("新建")) {
                convertView.findViewById(R.id.btn_start).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.item_time).setVisibility(View.INVISIBLE);
            }
            convertView.findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
        }



        Button btn_start = (Button) convertView.findViewById(R.id.btn_start);
        Button btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You clicked start of" + iData.get(groupPosition).get(childPosition).getiName(),
                        Toast.LENGTH_SHORT).show();

                final Intent intent = new Intent(nowActivity, LockActivity.class);
                int time=iData.get(groupPosition).get(childPosition).getiTime();
                intent.putExtra("timelong", time);
                int todo=iData.get(groupPosition).get(childPosition).getTodoId();
                intent.putExtra("usertodoid",todo);
                int set=iData.get(groupPosition).get(childPosition).getTodoSetId();
                intent.putExtra("usertodosetid",set);
                nowActivity.startActivity(intent);
                nowActivity.finish();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("确认");
                dialog.setMessage("是否确定删除 \"" + iData.get(groupPosition).get(childPosition).getiName() + "\" ?");
                // 对话框弹出后点击或按返回键不消失
                dialog.setCancelable(false);
                // 设定确定按钮的点击事件
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        final ProgressDialog progressDialog = new ProgressDialog(mContext);
                        progressDialog.setTitle("This is ProgressDialog");
                        progressDialog.setMessage("请稍后...");
                        // 数据加载完成后，调用ProgressDialog的dismiss()方法关闭对话框
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        JSONObject param = new JSONObject();
                        try {
                            param.put("teamId", iData.get(groupPosition).get(childPosition).getTeamId());
                            param.put("name", iData.get(groupPosition).get(childPosition).getiName());
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
                                .url(baseUrl + "/teamTodo/delete")
                                .post(requestBody)
                                .build();

                        Call task = client.newCall(request);
                        task.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                String error = e.getMessage().toString();
                                Log.d("team-delete", "error -> " + error);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                int code = response.code();
                                progressDialog.dismiss();
                                Log.d("team-delete", "code -> " + code);
                                if (code == HttpURLConnection.HTTP_OK) {
                                    ResponseBody body = response.body();
                                    if (body != null) {

                                        Log.d("team-delete", "result -> " + body.string());
                                    }
                                }
                            }
                        });
                        dialog.dismiss();
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
            }
        });
        convertView.setBackgroundColor(randomColor());
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup {
        private TextView group_name;
    }

    private static class ViewHolderItem {
        private TextView item_time;
        private TextView item_name;
        private boolean flag = false;
    }

    private int randomColor() {
        double num;
        num = Math.random() * 10;
        if (num >= 0 && num <2){
            return Color.rgb(135, 206, 255);
        }
        else if (num >= 2 && num < 4) {
            return Color.rgb(106, 90, 205);
        }
        else if (num >= 4 && num < 6) {
            return Color.rgb(32, 178, 170);
        }
        else if (num >= 6 && num < 8) {
            return Color.rgb(95, 158, 160);
        }
        else {
            return Color.rgb(255, 130, 71);
        }
    }
}

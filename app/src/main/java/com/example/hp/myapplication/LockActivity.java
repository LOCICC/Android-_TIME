package com.example.hp.myapplication;
import com.example.hp.myapplication.alldata.Data1;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

;import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LockActivity extends AppCompatActivity {


    private ProgressRing mProgressRing;
    private static String usertodoid;
    private static int sum;
    private static int all;
    private static int setid;
    private int mProgressSecond = 0;
    private int mProgressHour=0;
    private int minute=0;
    private final int MESSAGE_PROGRESS = 0;
    private static boolean flag=true;
    private static boolean f=false;
    public void inform (int s)throws JSONException
    {
        JSONObject param=new JSONObject();
        System.out.println("usertodoid==============="+usertodoid+"\n");
        param.put("todoId",usertodoid);
        param.put("todoStatusId",s);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(body )
                .url(Data1.url+"/userTodo/updateState")
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
                System.out.print("lock数据---------"+rtn+"\n");
            }

        } );
    }

    public void inform1 ()throws JSONException
    {
        JSONObject param=new JSONObject();
        System.out.println("usertodoid==============="+usertodoid+"\n");
        param.put("todoId",usertodoid);
        param.put("statusId",2);
        param.put("flag",0);
        param.put("time",sum);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(body )
                .url(Data1.url+"/record/setRecord")
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
                System.out.print("lock数据---------"+rtn+"\n");
            }

        } );
    }
    public void inform2 ()throws JSONException
    {
        JSONObject param=new JSONObject();
        System.out.println("usertodoid==============="+usertodoid+"\n");
        param.put("todoId",usertodoid);
        param.put("userTodoSetId",setid);
        param.put("statusId",2);
        param.put("flag",1);
        param.put("time",sum);
        String json=param.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(body )
                .url(Data1.url+"/record/setRecord")
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
                System.out.print("lock数据---------"+rtn+"\n");
            }

        } );
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_PROGRESS:
                    all--;
                    if (all < 0) {
                        // TODO 倒计时结束
                        try {
                            if(setid==0)inform1();
                            else inform2();
                            flag=true;
                            f=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LockActivity.this, ButtonActivity.class);
                        startActivity(intent);
                        return false;
                    }
                    mProgressHour=all/3600;
                    minute=(all%3600)/60;
                    mProgressSecond=all%60;
                    if(mProgressHour<10&&minute<10&&mProgressSecond<10) mProgressRing.setText("0"+mProgressHour+":0"+minute+":0"+mProgressSecond);
                    else if(mProgressHour>10&&minute<10&&mProgressSecond<10)mProgressRing.setText(mProgressHour+":0"+minute+":0"+mProgressSecond);
                    else if(mProgressHour<10&&minute>=10&&mProgressSecond<10) mProgressRing.setText("0"+mProgressHour+":"+minute+":0"+mProgressSecond);
                    else if(mProgressHour<10&&minute>=10) mProgressRing.setText("0"+mProgressHour+":"+minute+":"+mProgressSecond);
                    else if(mProgressHour<10&&minute<10)mProgressRing.setText("0"+mProgressHour+":0"+minute+":"+mProgressSecond);
                    else if(mProgressHour>10&&minute<10)mProgressRing.setText(mProgressHour+":0"+minute+":"+mProgressSecond);
                    else if(minute<10)mProgressRing.setText(mProgressHour+":"+minute+":0"+mProgressSecond);
                    else mProgressRing.setText(mProgressHour+":"+minute+":"+mProgressSecond);
                    mProgressRing.setProgress((float) (all * 100.0 / sum));
                    if(f)mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_PROGRESS, 1000);
                    break;
                case 1:
                    if (all < 0) {
                        return false;
                    }
                    mProgressHour=all/3600;
                    minute=(all%3600)/60;
                    mProgressSecond=all%60;
                    if(mProgressHour<10&&minute<10&&mProgressSecond<10) mProgressRing.setText("0"+mProgressHour+":0"+minute+":0"+mProgressSecond);
                    else if(mProgressHour>10&&minute<10&&mProgressSecond<10)mProgressRing.setText(mProgressHour+":0"+minute+":0"+mProgressSecond);
                    else if(mProgressHour<10&&minute>=10&&mProgressSecond<10) mProgressRing.setText("0"+mProgressHour+":"+minute+":0"+mProgressSecond);
                    else if(mProgressHour<10&&minute>=10) mProgressRing.setText("0"+mProgressHour+":"+minute+":"+mProgressSecond);
                    else if(mProgressHour<10&&minute<10)mProgressRing.setText("0"+mProgressHour+":0"+minute+":"+mProgressSecond);
                    else if(mProgressHour>10&&minute<10)mProgressRing.setText(mProgressHour+":0"+minute+":"+mProgressSecond);
                    else if(minute<10)mProgressRing.setText(mProgressHour+":"+minute+":0"+mProgressSecond);
                    else mProgressRing.setText(mProgressHour+":"+minute+":"+mProgressSecond);
                    mProgressRing.setProgress((float) (all * 100.0 / sum));
                    if(f)mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                    break;
                default:
                    break;
            }
            return false;
        }

    });


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        if(flag) {
            f=false;
                all = getIntent().getIntExtra("timelong", 5);
                usertodoid = getIntent().getStringExtra("usertodoid");
                setid=getIntent().getIntExtra("usertodosetid",0);
                System.out.print(all);
                all = all * 60;
                sum = all;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 100);


        Button tClick = findViewById(R.id.button);
        tClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LockActivity.this,
                        "打开白名单", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LockActivity.this, AppActivity.class);
                startActivity(intent);
               LockActivity.this.finish();
            }
        });

        Button tClick1 = findViewById(R.id.button4);
        tClick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inform1();
                    f=true;
                    flag=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LockActivity.this,
                        "锁屏终止", Toast.LENGTH_SHORT).show();
                mHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(LockActivity.this, ButtonActivity.class);
                startActivity(intent);
                LockActivity.this.finish();
            }
        });

        this.getWindow().setFlags(0x80000000, 0x80000000);
        prohibitDropDown();
        hideNavigationBar();
        ListenRecentAndHome();
        mProgressRing = findViewById(R.id.pr_progress);
        if(flag){
            mHandler.sendEmptyMessage(MESSAGE_PROGRESS);
            flag = false;
        }
        else{
            mHandler.sendEmptyMessage(1);
        }

    }


    protected void hideBottomUIMenuV2() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    protected void hideBottomUIMenu() {
        int flags;
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        // This work only for android 4.4+
        if(curApiVersion >= Build.VERSION_CODES.KITKAT){
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }else{
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }

        // must be executed in main thread :)
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
    public void hideNavigationBar() {

        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;// hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            //请自行查询 IMMERSIVE和IMMERSIVE_STICKY的区别
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY ;//|View.SYSTEM_UI_FLAG_IMMERSIVE;//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hid
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        try {

            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    //禁止下拉
    private void prohibitDropDown() {
        manager = ((WindowManager)getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        view = new LinearLayout(this);
        view.setBackgroundColor(0);
        manager.addView(view, localLayoutParams);
    }
    LinearLayout view;
    WindowManager manager;

    @Override
    protected void onDestroy() {

        super.onDestroy();
        manager.removeView(view);

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Log.d("DeviceKeyMonitor", "按了返回键");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onHomeClick() {
        //Toast.makeText(this,"点击了Home键",Toast.LENGTH_LONG).show();
        Log.d("DeviceKeyMonitor", "按了Home键");
    }

    public void onRecentClick() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
    private BroadcastReceiver recentReceiver;
    private static final String SYSTEM_REASON = "reason";
    private static final String SYSTEM_HOME_KEY = "homekey";
    private static final String SYSTEM_HOME_RECENT_APPS = "recentapps";
    public void ListenRecentAndHome(){

        recentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                    String reason = intent.getStringExtra(SYSTEM_REASON);
                    if (!TextUtils.isEmpty(reason)){
                        if (SYSTEM_HOME_KEY.equals(reason)){
                            onHomeClick();
                        } else if (SYSTEM_HOME_RECENT_APPS.equals(reason)){
                            onRecentClick();
                        }
                    }
                }
            }
        };
        this.registerReceiver(recentReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.registerReceiver(recentReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(recentReceiver);

    }
    //打开默认主页面设置Activity
    public void gotoSetDefaultHomePage(View view){
        Intent intent=new Intent(Settings.ACTION_HOME_SETTINGS);
        startActivity(intent);
    }
    public void gotoSetDisplay(View view) {
        Intent intent=new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        startActivity(intent);
    }
    public void gotoSetNetWork(View view) {
        Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }
    public void gotoSetApplicationSettings(View view) {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        startActivity(intent);
    }

    public void gotoSettings(View view){
        Intent intent=new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }
    public void gotoSettings(String activityName){
        //下面这句用于测试
        activityName="com.android.settings.WirelessSettings";
        Intent intent = new Intent();
        ComponentName cm = new ComponentName("com.android.settings",activityName);
        intent.setComponent(cm);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }

}

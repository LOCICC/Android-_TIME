package com.example.hp.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.alldata.Data1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.lang.Integer.valueOf;

@SuppressLint("ValidFragment")
public class Timefragment extends Fragment
{
    private LineChartView lineChart;
    int day ;//X轴的标注个数(对应月份)
    String baseUrl ="http://5a774f3c501a.ngrok.io";

    //切换测试数据
    //int[] days = {31,29,31,30,31,30,31,31,30,31,30,31};
    //切换月份节点测试数据
    //int[] test = {100,0,30,200,55,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    //初始化节点
    int[] date = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    private static final int msgKey1 = 1;
    private TextView mTime;

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
    List<Line> lines = new ArrayList<Line>();

    Message message = Message.obtain();
    Message message1 = Message.obtain();
    Message message2 = Message.obtain();
    Message message3 = Message.obtain();
    private String[] Data={"学习","打扫"};//相应的待办
    private int []a;
    private String acctime=null;
    private String daytime=null;
    private String monthtime=null;
    private AlertDialog alertDialog1;
    @SuppressLint("ValidFragment")
    private TextView ac;
    private TextView day_;
    private TextView month;

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ac.setText(acctime);
            }
        }
    };

    private Handler handler1= new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what== 2) {
            day_.setText(daytime);
        }
    }
};
    private Handler handler2= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3) {
                month.setText(monthtime);
            }
        }
    };

    private Handler handler3= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 4) {
                mPointValues.clear();
                getAxisLables();
                //获取服务器传回的坐标点
                for (int i = 0; i < day; i++) {
                    mPointValues.add(new PointValue(i, date[i]));
                }
                initLineChart();
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
                    daytime=String.valueOf(s);
                    message1.what = 2;
                    handler1.sendMessage(message1);
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
                    monthtime=String.valueOf(s);
                    message2.what = 3;
                    handler2.sendMessage(message2);
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
        day_=messageLayout.findViewById(R.id.textView6);
        month=messageLayout.findViewById(R.id.textView16);
        ac.setText(acctime);
        day_.setText(daytime);
        month.setText(monthtime);

        lineChart = (LineChartView)messageLayout.findViewById(R.id.line_chart);
//        mTime =(TextView)messageLayout.findViewById(R.id.month);
//        //new TimeThread().start();//实时更新年月线程，如果需要切换页面就不能用
//        mTime.setText(getTime());
        getDay();
        getTime();
        getAxisLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        //连接服务器
        sendRequestWithOkHttp();

        return  messageLayout;
    }

    private void sendRequestWithOkHttp() {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url(Data1.url+"/record/getDailyRecordByMonth?date_1=2020-04-01&date_2=2020-05-01")
                            .addHeader("id",Data1.ID)
                            .addHeader("token",Data1.Token)
                            .build();
                client.newCall(request).enqueue(new Callback() {
                    public void onFailure(Call call, IOException e) {
                        // 请求失败
                        String err = e.getMessage().toString();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        String responseData=response.body().string();
                        Log.i("okHttp的请求结果：" , responseData);
                            if(responseData!= null) {
                                try{
                                    JSONObject jsonobj=new JSONObject(responseData);
                                    for(int i=0;i< 31;i++) {
                                        String a = String.valueOf(i+1);
                                        int time = jsonobj.getInt(a);
                                        date[i] = time;
                                        System.out.println(a+"-----"+date[i]+"\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                message3.what = 4;
                                handler3.sendMessage(message3);

                            }
                        }

                });


    }


    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.CYAN).setCubic(false);  //折线的颜色
        //List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(7);//设置字体大小
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部

        Axis axisY = new Axis();  //Y轴
        axisY.setTextColor(Color.BLACK);  //设置字体颜色
        axisY.setName("专注时长(min)");//y轴标注
        axisY.setTextSize(7);//设置字体大小
        List<AxisValue> yValues = new ArrayList<>();
        for(int i = 0; i < 144; i++){
            AxisValue yValue = new AxisValue(10*i);
            yValues.add(yValue);
        }
        axisY.setValues(yValues);
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        //支持横向滑动
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        lineChart.setMaximumViewport(v);
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisLables(){
        getDay();
        for (int i = 0; i < day; i++) {
            mAxisValues.add(new AxisValue(i).setLabel(String.valueOf(i+1)));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < day; i++) {
            mPointValues.add(new PointValue(i, date[i]));
        }
    }


    //获得当前年月
    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mYear =  valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        int mMonth = valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        return mYear + "-" + mMonth ;
    }


    //获得当前月份的天数
    public int getDay(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mYear =  valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        int mMonth = valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份

        if(mYear%4 == 0 && mYear%100!=0 || mYear%400 == 0){//是闰年
            if(mMonth==1||mMonth==3||mMonth==5||mMonth==7||mMonth==8||mMonth==10||mMonth==12){
                day = 31;
            }else if(mMonth==2){
                day = 29;
            }else {
                day = 30;
            }
        }else{
            if(mMonth==1||mMonth==3||mMonth==5||mMonth==7||mMonth==8||mMonth==10||mMonth==12){
                day = 31;
            }else if(mMonth==2){
                day = 28;
            }else{
                day = 30;
            }
        }
        return day ;
    }



}
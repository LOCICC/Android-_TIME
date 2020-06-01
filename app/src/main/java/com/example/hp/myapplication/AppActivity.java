package com.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.alldata.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class AppActivity extends AppCompatActivity{


    private static ListView mlistview;
    private static List<AppInfo> applicationInfoList;
    private static MyAdapter myAdapter;
    private static PackageManager pm;
    private static boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        mlistview=findViewById(R.id.lv_app_list);
        if(flag)getAllAppNames();
        myAdapter=new MyAdapter(applicationInfoList);
        mlistview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        Button button=findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;
                Toast.makeText(AppActivity.this,
                        "继续", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AppActivity.this, LockActivity.class);
                startActivity(intent);
                //        mHandler.removeCallbacksAndMessages(null);
            }
        });

//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                doStartApplicationWithPackageName(applicationInfoList.get(position).getPackageName());
//            }
//        });
        //listView长按事件
        mlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(AppActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("===================\n"+applicationInfoList.size());
                        if(applicationInfoList.remove(position)!=null){
                            System.out.println("success");
                        }else {
                            System.out.println("failed");
                        }
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return false;
            }
        });
    }


    //自定义适配器
    public class MyAdapter extends BaseAdapter{

        private List<AppInfo> list;

        public MyAdapter(List<AppInfo> list){
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view=null;
            if(convertView==null){
                view=LayoutInflater.from(AppActivity.this).inflate(R.layout.mlistview_layout,null);
            }else{
                view=convertView;
            }
            TextView tv_packagename=view.findViewById(R.id.tv_packagename);
            TextView tv_appname=view.findViewById(R.id.tv_appname);
            ImageView img=view.findViewById(R.id.img);
            tv_appname.setText(list.get(position).getName());
            tv_packagename.setText(list.get(position).getPackageName());
            img.setImageDrawable(list.get(position).getIcon());
            ImageView inter=view.findViewById(R.id.inter);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doStartApplicationWithPackageName(applicationInfoList.get(position).getPackageName());
                }
            });

            return view;
        }
    }

    public void getAllAppNames(){
        pm=getPackageManager();
        ////获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        // packageInfoList=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        applicationInfoList=new ArrayList<>();
        List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(list, new ApplicationInfo.DisplayNameComparator(pm));// 排序
        Log.e("-------",list.size()+"");
        applicationInfoList.clear();
        for (int i=0;i<list.size();i++) {
            //非系统程序
            if((list.get(i).flags&ApplicationInfo.FLAG_SYSTEM)==0) {
                applicationInfoList.add(getAppInfo(list.get(i)));//如果非系统应用，则添加至appList

            }else{
                //系统程序
            }
        }
        System.out.println("-----------------------\n"+applicationInfoList.size());
    }

    private void doStartApplicationWithPackageName(String packagename) {
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packagename);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if(resolveinfoList.size() == 0){
            return;
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            //App启动的Activity
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:启动的Activity
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    // 构造一个AppInfo对象 ，并赋值
    private AppInfo getAppInfo(ApplicationInfo app) {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(app.loadLabel(pm).toString());
        appInfo.setIcon(app.loadIcon(pm));
        appInfo.setPackageName(app.packageName);
        return appInfo;
    }

}

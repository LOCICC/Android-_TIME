package com.example.hp.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.myapplication.R;

import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter implements OnClickListener {
    private List<Map<String,Object>> list;
    private Context mContext;
    private InnerItemOnclickListener mListener;

    public MyAdapter(List<Map<String,Object>> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
        }
        else {
            convertView = convertView;
            mHolder = (ViewHolder) convertView.getTag();  //重新获得ViewHolder
        }

        if(list.get(position).get("status").toString().equals("1")){
            System.out.println("you_____-----------"+list.get(position).get("status").toString()+"----"+list.get(position).get("title").toString());
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,
                    null);
        }
        else if(list.get(position).get("status").toString().equals("3")){
            System.out.println("you_____-----------"+list.get(position).get("status").toString()+"----"+list.get(position).get("title").toString());
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item1,
                    null);
        }
        else {
            System.out.println("you_____-----------"+list.get(position).get("status").toString()+"----"+list.get(position).get("title").toString());
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item2,
                    null);
        }
        mHolder = new ViewHolder();
        mHolder.card_title = (TextView) convertView.findViewById(R.id.cardTitle);
        mHolder.card_time = (TextView) convertView.findViewById(R.id.cardTime);
        mHolder.card_begin = (TextView) convertView.findViewById(R.id.cardBegin);
        convertView.setTag(mHolder);  //将ViewHolder存储在View中
        mHolder.card_begin.setOnClickListener(this);
        mHolder.card_begin.setTag(position);

        mHolder.card_title.setText(list.get(position).get("title").toString());
        mHolder.card_time.setText(list.get(position).get("time").toString());
        mHolder.card_begin.setText(list.get(position).get("begin").toString());

        return convertView;
    }

    class ViewHolder {
        TextView card_title;
        TextView card_time;
        TextView card_begin;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

}

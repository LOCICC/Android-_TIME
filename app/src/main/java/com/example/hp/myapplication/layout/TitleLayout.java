package com.example.hp.myapplication.layout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.myapplication.R;
import com.example.hp.myapplication.TeamsListActivity;

public class TitleLayout extends LinearLayout {
    public TitleLayout (final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);

        if (context.getClass().getSimpleName().equals("TeamsListActivity")) {
            ImageButton btn_create = findViewById(R.id.btn_create);
            btn_create.setVisibility(VISIBLE);

            btn_create.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamsListActivity teamsListActivity = (TeamsListActivity) getContext();
                    teamsListActivity.showDialog();
                }
            });
        }
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }



}

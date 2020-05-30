package com.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PersActivity extends AppCompatActivity {

    private TextView edit1;
    private View button1;
    private TextView edit2;
    private View button2;
    private TextView edit3;
    private View button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers);
        edit1=findViewById(R.id.textView11);
        button1=findViewById(R.id.image11);
        edit2=findViewById(R.id.textView22);
        button2=findViewById(R.id.image12);
        edit3=findViewById(R.id.textView33);
        button3=findViewById(R.id.image13);
    }

    public void r1(final View view)
    {
        final EditText inputServer = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改信息").setIcon(R.drawable.edit).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(view==button1){
                    edit1.setText(inputServer.getText().toString());
                }
                else if(view==button2) {
                    edit2.setText(inputServer.getText().toString());
                }
                else{
                    edit3.setText(inputServer.getText().toString());
                }
            }
        });
        builder.show();
    }

}

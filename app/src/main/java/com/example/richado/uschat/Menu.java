package com.example.richado.uschat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences user_info = getSharedPreferences("setting",0);
        String name = user_info.getString("name","");
        System.out.println(name);
        TextView textView = (TextView) findViewById(R.id.text_welcome);
        textView.setText("嗨！"+name+",开始聊天吧");

//        按钮事件
        Button btnlisten=(Button) findViewById(R.id.button_listen);
        Button btntell = (Button) findViewById(R.id.button_tell);
        Button btnset = (Button) findViewById(R.id.button_setting);
        Button btnquit = (Button) findViewById(R.id.button_quit);

//        我要听事件，跳转页面
        btnlisten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(Menu.this, ChatActivity.class);
                startActivity(intent);
            }
        });

//        我要说事件，跳转页面
        btntell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(Menu.this, ChatActivity.class);
                startActivity(intent);
            }
        });

//        设置页面跳转
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(Menu.this,SetActivity .class);
                startActivity(intent);
            }
        });

//        退出
        btnquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }
}

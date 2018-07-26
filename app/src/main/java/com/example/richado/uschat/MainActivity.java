package com.example.richado.uschat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText ed_name;
    EditText ed_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到id和pwd
        ed_name=(EditText)findViewById(R.id.reg_name);
        ed_pwd=(EditText)findViewById(R.id.pwd);

        //找到注册和登陆按钮
        Button btnlogin=(Button)findViewById(R.id.login);
        Button btnreg = (Button)findViewById(R.id.reg_new);

        //登陆监听事件
        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getUser();
            }
        });

        //注册监听事件
        btnreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, UserRegister.class);
                startActivity(intent);
            }
        });
    }
    public void getUser()
    {
        String sql="select * from user where userid=?";
        Cursor cursor=database.getWritableDatabase().rawQuery(sql, new String[]{ed_id.getText().toString()});
        if(cursor.moveToFirst())
        {

            if(ed_pwd.getText().toString().equals(cursor.getString(cursor.getColumnIndex("userpwd"))))
            {
                Toast.makeText(this, "登录成功", 5000).show();
            }
            else
            {
                Toast.makeText(this, "用户名或者密码错误", 5000).show();
            }
        }
        database.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

登录界面代码
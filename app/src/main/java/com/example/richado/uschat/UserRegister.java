package com.example.richado.uschat;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegister extends Activity {

    //注册用户，密码，确认密码
    EditText edtext;
    EditText edpwd;
    EditText edpwd2;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //注册界面的注册按钮
        Button btnsub=(Button)findViewById(R.id.reg_new);
        //注册界面三个变量
        edtext=(EditText)findViewById(R.id.reg_name);
        edpwd=(EditText)findViewById(R.id.reg_pwd1);
        edpwd2=(EditText)findViewById(R.id.reg_pwd2);

        //注册按钮监听
        btnsub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setUser();
            }
        });
//        Button btncancel=(Button)findViewById(R.id.btncancel);
//        btncancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                finish();
//            }
//        });

    }
    private void setUser()
    {
        DBHelper database=new DBHelper(UserRegister.this,"LoginInfo",null,1);


        if(edtext.getText().toString().length()<=0||edpwd.getText().toString().length()<=0||edpwd2.getText().toString().length()<=0)
        {
            Toast.makeText(this, "用户名或密码不能为空", 5000).show();
            return;
        }
        if(edtext.getText().toString().length()>0)
        {
            String sql="select * from user where userid=?";
            Cursor cursor=database.getWritableDatabase().rawQuery(sql, new String[]{edtext.getText().toString()});
            if(cursor.moveToFirst())
            {
                Toast.makeText(this, "用户名已经存在", 5000).show();
                return;
            }
        }
        if(!edpwd.getText().toString().equals(edpwd2.getText().toString()))
        {
            Toast.makeText(this, "两次输入的密码不同", 5000).show();
            return;
        }
        if(database.AddUser(edtext.getText().toString(), edpwd.getText().toString()))
        {
            Toast.makeText(this, "用户注册成功", 5000).show();
            Intent intent=new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "用户注册失败", 5000).show();
        }
        database.close();
    }

}

界面注册
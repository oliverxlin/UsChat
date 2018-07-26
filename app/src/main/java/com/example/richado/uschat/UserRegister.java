package com.example.richado.uschat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;



public class UserRegister extends Activity {
    private static String PATH ="123.206.45.190:5000/register/";
    //注册用户，密码，确认密码
    EditText ed_name;
    EditText ed_pwd;
    EditText ed_pwd2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //注册界面的注册按钮
        Button btnsub = (Button) findViewById(R.id.reg_new);
        //注册界面三个变量
        ed_name = (EditText) findViewById(R.id.reg_name);
        ed_pwd = (EditText) findViewById(R.id.reg_pwd1);
        ed_pwd2 = (EditText) findViewById(R.id.reg_pwd2);

        //注册按钮监听
        btnsub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String name = ed_name.getText().toString();
                String pwd = ed_pwd.getText().toString();
                String pwd2 = ed_pwd2.getText().toString();
                PostThread postThread = new PostThread(name, pwd, pwd2);
                postThread.start();
            }
        });


    }

    //子线程：使用POST方法向服务器发送用户名、密码等数据
    class PostThread extends Thread {

        String name;
        String pwd;
        String pwd2;

        public PostThread(String name, String pwd,String pwd2) {
            this.name = name;
            this.pwd = pwd;
            this.pwd2 = pwd2;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = PATH;
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对

            JSONObject params = new JSONObject();
            try {
                params.put("name", this.name);
                params.put("pwd", this.pwd);
//		...
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity;
            try {
                //创建代表请求体的对象（注意，是请求体）
                entity = new StringEntity(params.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");

                //将请求体放置在请求对象当中
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                //执行请求对象
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity_rep = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity_rep.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
package com.example.richado.uschat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Entity;
import android.os.Handler;
import android.os.Message;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.StrictMode;
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

public class MainActivity extends Activity {

    private static String PATH ="http://47.94.219.255:8080/register/";
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
                String name = ed_name.getText().toString();
                String pwd = ed_pwd.getText().toString();
                PostThread postThread = new PostThread(name, pwd);
                postThread.start();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, ChatActivity.class);
                startActivity(intent);
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

    //子线程：使用POST方法向服务器发送用户名、密码等数据
    class PostThread extends Thread {

        String name;
        String pwd;

        public PostThread(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
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
                params.put("name",this.name);
                params.put("pwd",this.pwd);
//		...
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity;
            try {
                //创建代表请求体的对象（注意，是请求体）
                entity = new StringEntity(params.toString(),"utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");

                //将请求体放置在请求对象当中
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-type","application/json");
                httpPost.setHeader("Accept", "application/json");
                //执行请求对象
                try {

                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        String result = EntityUtils.toString(response.getEntity());
                        //解析json数据
//                        JSONArray result_array = new JSONArray(result);
//                        for(int i = 0;i < result_array.length();i++){
//                            JSONObject jsonob = result_array.getJSONObject(i);
//                            String rep_name = jsonob.getString("name");
//                            String rep_pwd = jsonob.getString("pwd");
//                        }
//                       获取二层嵌套数据


                        JSONObject jsonob_1 = new JSONObject(result);
                        String rep_msg = jsonob_1.getString("msg");
                        JSONObject jsonob_2 = new JSONObject(rep_msg);
                        String rep_name = jsonob_2.getString("name");
                        String rep_pwd = jsonob_2.getString("pwd");
                        Log.d("HTTP", "POST:" +"name:"+rep_name+"pwd:"+rep_pwd);
//                        页面跳转
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this, ChatActivity.class);
                        startActivity(intent);
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

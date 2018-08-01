package com.example.richado.uschat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
                AlertDialog.Builder builder= new AlertDialog.Builder(Menu.this);
                //设置内容
                builder.setIcon(R.mipmap.ic_launcher);
                //设置头像Icon
                builder.setView(R.layout.wait_login);
                builder.show();
                SharedPreferences user_info = getSharedPreferences("setting",0);
                String name = user_info.getString("name","");
                String token = user_info.getString("token","");
                JSONObject data = new JSONObject();
                try {
                    data.put("character", "talker");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PostThread postThread =new PostThread(name, token, data);
                postThread.start();
                MatchThread matchThread =new MatchThread(name,token);
                matchThread.start();
                ConnectThread connectThread = new ConnectThread(name,token);
                connectThread.start();
            }
        });

//        我要说事件，跳转页面
        btntell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder= new AlertDialog.Builder(Menu.this);
                //设置内容
                builder.setIcon(R.mipmap.ic_launcher);
                //设置头像Icon
                builder.setView(R.layout.wait_login);
                builder.show();
                SharedPreferences user_info = getSharedPreferences("setting",0);
                String name = user_info.getString("name","");
                String token = user_info.getString("token","");
                JSONObject data = new JSONObject();
                try {
                    data.put("character", "talker");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PostThread postThread =new PostThread(name, token, data);
                postThread.start();
                MatchThread matchThread =new MatchThread(name,token);
                matchThread.start();
                ConnectThread connectThread = new ConnectThread(name,token);
                connectThread.start();

            }
        });

//        设置页面跳转
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(Menu.this,SetActivity.class);
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

    class PostThread extends Thread {

        String name;
        String token;
        JSONObject content;

        public PostThread(String name, String token,JSONObject content) {
            this.name = name;
            this.token = token;
            this.content = content;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://47.94.219.255:8080/confide/";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对

            JSONObject params = new JSONObject();
            try {
                params.put("username", this.name);
                params.put("token", this.token);
                params.put("data", this.content);
//		...
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("tag"+params);
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
                        String rep_status = jsonob_1.getString("status");
                        Log.d("HTTP", "POST:" +"msg:" + rep_msg+"status:" + rep_status+"回答的请求");

//                        页面跳转

                        //Intent intent = new Intent();
                        //intent.setClass(Menu.this,com.example.richado.uschat.Menu.class);
                        //startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class MatchThread extends Thread{
        String name;
        String token;

        public MatchThread(String name, String token) {
            this.name = name;
            this.token = token;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://47.94.219.255:8080/matched/";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对

            JSONObject params = new JSONObject();
            try {
                params.put("username", this.name);
                params.put("token", this.token);
//		...
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("tag"+params);
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
                        String rep_status = jsonob_1.getString("status");
                        Log.d("HTTP", "POST:" +"msg:" + rep_msg+"status:" + rep_status+"回答的请求");

//                        页面跳转
                        if(rep_msg.contains("yes"))
                        {
                            //PostThread MatchThread =new PostThread(name, token, data);
                            //MatchThread.start();
                            System.out.println("匹配成功");
                        }
                        else
                        {
                            System.out.println("匹配失败");
                            Intent intent = new Intent();
                            intent.setClass(Menu.this,com.example.richado.uschat.Menu.class);
                            startActivity(intent);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class ConnectThread extends Thread {

        String name;
        String token;

        public ConnectThread(String name, String token) {
            this.name = name;
            this.token = token;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://47.94.219.255:8080/connect/";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对

            JSONObject params = new JSONObject();
            try {
                params.put("username", this.name);
                params.put("token", this.token);
//		...
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("tag"+params);
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
                        String rep_status = jsonob_1.getString("status");
                        Log.d("HTTP", "POST:" +"msg:" + rep_msg+"status:" + rep_status+"连接的请求");

//                      页面跳转
                        Intent intent = new Intent();
                        intent.setClass(Menu.this,com.example.richado.uschat.ChatActivity.class);
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


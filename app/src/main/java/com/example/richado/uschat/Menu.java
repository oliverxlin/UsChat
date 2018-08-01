package com.example.richado.uschat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
    private static String PATH ="http://47.94.219.255:8080/confide/";
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
                    data.put("character", "listener");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PostThread postThread =new PostThread(name, token, data);
                postThread.start();
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
            String url = PATH;
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

                        if(rep_msg.equals("{\"success\":\"waiting in the pool!\"}"))
                        {
                            HttpClient httpClient_2 = new DefaultHttpClient();
                            String url_2 = "http://47.94.219.255:8080/matched/";
                            //第二步：生成使用POST方法的请求对象
                            HttpPost httpPost_2 = new HttpPost(url_2);
                            //NameValuePair对象代表了一个需要发往服务器的键值对

                            JSONObject params_2 = new JSONObject();
                            try {
                                params_2.put("username", this.name);
                                params_2.put("token", this.token);
//		...
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("tag"+params);
                            StringEntity entity_2;
                            try {
                                //创建代表请求体的对象（注意，是请求体）
                                entity_2 = new StringEntity(params.toString(), "utf-8");
                                entity_2.setContentEncoding("UTF-8");
                                entity_2.setContentType("application/json");

                                //将请求体放置在请求对象当中
                                httpPost_2.setEntity(entity);
                                httpPost_2.setHeader("Content-type", "application/json");
                                httpPost_2.setHeader("Accept", "application/json");
                                //执行请求对象
                                try {

                                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                                    //第三步：执行请求对象，获取服务器发还的相应对象
                                    HttpResponse response_2 = httpClient_2.execute(httpPost_2);
                                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                                    if (response_2.getStatusLine().getStatusCode() == 200) {
                                        //第五步：从相应对象当中取出数据，放到entity当中
                                        String result_2 = EntityUtils.toString(response_2.getEntity());
                                        //解析json数据
//                        JSONArray result_array = new JSONArray(result);
//                        for(int i = 0;i < result_array.length();i++){
//                            JSONObject jsonob = result_array.getJSONObject(i);
//                            String rep_name = jsonob.getString("name");
//                            String rep_pwd = jsonob.getString("pwd");
//                        }
//                       获取二层嵌套数据


                                        JSONObject jsonob_12 = new JSONObject(result_2);
                                        String rep_msg_2 = jsonob_12.getString("msg");
                                        String rep_status_2 = jsonob_12.getString("status");
                                        Log.d("HTTP", "POST:" + "msg:" + rep_msg_2 + "status:" + rep_status_2 + "匹配的请求");

                                        if (rep_msg_2.contains("yes")) {
                                            String partner = jsonob_12.getJSONObject("msg").getString("partner");
                                            SharedPreferences user_info = getSharedPreferences("setting",0 );
                                            SharedPreferences.Editor editor = user_info.edit();
                                            editor.putString("matchedname", partner);
                                            editor.commit();
                                            HttpClient httpClient_3 = new DefaultHttpClient();
                                            String url_3 = "http://47.94.219.255:8080/connect/";
                                            //第二步：生成使用POST方法的请求对象
                                            HttpPost httpPost_3 = new HttpPost(url_3);
                                            //NameValuePair对象代表了一个需要发往服务器的键值对

                                            JSONObject params_3 = new JSONObject();
                                            try {
                                                params_3.put("username", this.name);
                                                params_3.put("token", this.token);
//		...
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println("tag" + params);
                                            StringEntity entity_3;
                                            try {
                                                //创建代表请求体的对象（注意，是请求体）
                                                entity_3 = new StringEntity(params.toString(), "utf-8");
                                                entity_3.setContentEncoding("UTF-8");
                                                entity_3.setContentType("application/json");

                                                //将请求体放置在请求对象当中
                                                httpPost_3.setEntity(entity);
                                                httpPost_3.setHeader("Content-type", "application/json");
                                                httpPost_3.setHeader("Accept", "application/json");
                                                //执行请求对象
                                                try {

                                                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                                                    //第三步：执行请求对象，获取服务器发还的相应对象
                                                    HttpResponse response_3 = httpClient_3.execute(httpPost_3);
                                                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                                                    if (response_3.getStatusLine().getStatusCode() == 200) {
                                                        //第五步：从相应对象当中取出数据，放到entity当中
                                                        String result_3 = EntityUtils.toString(response_3.getEntity());
                                                        //解析json数据
//                        JSONArray result_array = new JSONArray(result);
//                        for(int i = 0;i < result_array.length();i++){
//                            JSONObject jsonob = result_array.getJSONObject(i);
//                            String rep_name = jsonob.getString("name");
//                            String rep_pwd = jsonob.getString("pwd");
//                        }
//                       获取二层嵌套数据


                                                        JSONObject jsonob_13 = new JSONObject(result_3);
                                                        String rep_msg_3 = jsonob_13.getString("msg");
                                                        String rep_status_3 = jsonob_13.getString("status");
                                                        Log.d("HTTP", "POST:" + "msg:" + rep_msg_3 + "status:" + rep_status_3 + "连接的请求");
                                                        //切换画面
                                                        Intent intent = new Intent();
                                                        intent.setClass(Menu.this, com.example.richado.uschat.ChatActivity.class);
                                                        startActivity(intent);


                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Intent intent = new Intent();
                                        intent.setClass(Menu.this, com.example.richado.uschat.Menu.class);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Intent intent = new Intent();
                        intent.setClass(Menu.this, com.example.richado.uschat.ChatActivity.class);
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

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
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class SetActivity extends AppCompatActivity {
    private static String PATH ="http://47.94.219.255:8080/register/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Button btn_change_name = (Button)findViewById(R.id.change_name);
        Button btn_change_pwd = (Button)findViewById(R.id.change_pwd);
        Button btn_change_3 = (Button)findViewById(R.id.change_3);

        btn_change_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder= new AlertDialog.Builder(SetActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setView(R.layout.change_name);
                builder.show();
                EditText change_name = (EditText) findViewById(R.id.change_name_text);
                String name = change_name.getText().toString();
//                PostThread postThread = new PostThread(name);
//                postThread.start();
            }
        });
        btn_change_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder= new AlertDialog.Builder(SetActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setView(R.layout.change_pwd);
                builder.show();
                EditText change_name = (EditText) findViewById(R.id.change_pwd1_text);
                String name = change_name.getText().toString();
//                PostThread postThread = new PostThread(name);
//                postThread.start();
            }
        });
    }
    //子线程：使用POST方法向服务器发送用户名、密码等数据
    class PostThread extends Thread {

        String name;
        String pwd;

        public PostThread(String name) {
            this.name = name;
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
                params.put("username",this.name);
                params.put("password",this.pwd);
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
//

                        JSONObject jsonob_1 = new JSONObject(result);
                        String rep_msg = jsonob_1.getString("msg");
                        String rep_status = jsonob_1.getString("status");
                        Log.d("HTTP", "POST:" +"msg:" + rep_msg+"status:" + rep_status);

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

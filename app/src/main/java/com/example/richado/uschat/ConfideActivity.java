package com.example.richado.uschat;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class ConfideActivity  extends Activity {
    private static String PATH ="http://47.94.219.255:8080/confide/";
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatmain_layout);

        initMsgs();//初始化消息数据
        adapter = new MsgAdapter(ConfideActivity.this, R.layout.msg_layout, msgList);
        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgListView = (ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        //发送按钮的点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//当有消息时刷新
                    msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                    inputText.setText("");//清空输入框的内容
                    SharedPreferences user_info = getSharedPreferences("setting",0);
                    String name = user_info.getString("name","");
                    String token = user_info.getString("token","");
                    JSONObject data = new JSONObject();
                    try {
                        data.put("character", "talker");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(name+token+data);
                    PostThread postThread = new PostThread(name, token, data);
                    postThread.start();
                }
            }
        });

    }

    /**
     * 初始化消息数据
     * */
    private void initMsgs(){
        SharedPreferences user_info = getSharedPreferences("setting",0);
        String name = user_info.getString("name","");
        Msg msg1 = new Msg("欢迎 "+name+"\n"+"你可以尽情倾诉", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
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
                    Intent intent=new Intent();
                    intent.setClass(ConfideActivity.this, com.example.richado.uschat.Menu.class);
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
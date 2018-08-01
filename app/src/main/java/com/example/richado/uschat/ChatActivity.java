package com.example.richado.uschat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();
    SharedPreferences user_info = getSharedPreferences("setting",0);
    final String name = user_info.getString("name","");
    final String matched_name=user_info.getString("matchedname","");
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatmain_layout);
        try {
            final SocketClient client = new SocketClient("123.206.45.190", 7199);
            client.SendMessage("username:" + name);
            adapter = new MsgAdapter(ChatActivity.this, R.layout.msg_layout, msgList);
            inputText = (EditText) findViewById(R.id.input_text);
            send = (Button) findViewById(R.id.send);
            msgListView = (ListView) findViewById(R.id.msg_list_view);
            msgListView.setAdapter(adapter);

            //发送按钮的点击事件
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = inputText.getText().toString();
                    content=name+":\n"+content;
                    if (!"".equals(content)) {
                        Msg msg = new Msg(content, Msg.TYPE_SENT);
                        msgList.add(msg);
                        //传给server
                        try {
                            client.SendMessage(content);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();//当有消息时刷新
                        msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                        inputText.setText("");//清空输入框的内容
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Receive extends Thread
    {
        SocketClient client;
        public Receive(SocketClient s){
            client=s;
        }

        @Override
        public void run() {
            while(true){
                try{
                    String content=client.ReceiveMessage();
                    content=matched_name+":\n"+content;
                    Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//当有消息时刷新
                    msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 初始化消息数据
     * */

}

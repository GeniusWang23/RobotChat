package cn.itcast.robotchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ChatAdapter adapter;
    private List<ChatBean> chatBeanList;

    private EditText et_send_msg;
    private Button btn_send;

    private static final String WEB_SITE = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    private String sendMsg;
    private String[] welcome;
    private MHandler mHandler;
    public static final int MSG_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        chatBeanList = new ArrayList<ChatBean>();
        mHandler = new MHandler();
        //
        welcome = getResources().getStringArray(R.array.welcome);
        initView();
    }

    private void initView() {
        listView = findViewById(R.id.list);
        et_send_msg = findViewById(R.id.et_send_msg);
        btn_send = findViewById(R.id.btn_send);
        //adapter = new ChatAdapter(chatBeanList, this);
        listView.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        et_send_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendData();
                }
                return false;
            }
        });

        int position = (int)(Math.random() * welcome.length);
        showData(welcome[position]);
    }

    private void sendData() {
        //将输入数据送入ListView
        sendMsg = et_send_msg.getText().toString().trim();
        if(TextUtils.isEmpty(sendMsg)){
            Toast.makeText(this, "你还没有输入任何信息哦！", Toast.LENGTH_LONG).show();
            return;
        }

        //清空输入框
        et_send_msg.setText("");

        ChatBean chatBean = new ChatBean();
        chatBean.setMessage(sendMsg);
        chatBean.setState(ChatBean.SEND);
        chatBeanList.add(chatBean);

        adapter.notifyDataSetChanged();
        //将数据封装向服务器发请求
        getDataFromServer();
    }
    private void getDataFromServer(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(WEB_SITE + sendMsg).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(@NotNull Call call,IOException e)  {

            }

        });
    }           
    class MHandler extends Handler{
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case MSG_OK:
                    if (msg.obj!=null){
                        String vlResult = (String)msg.obj;
                        paresData(vlResult);
                    }
                    break;
            }
        }
    }

    private void paresData(String vlResult) {
        try {
            JSONObject obj = new JSONObject(vlResult);
            String content = obj.getString("content");
            int code = obj.getInt("result");
            updateView(code,content);
        } catch (JSONException e) {
            e.printStackTrace();
            showData("主人，你的网络不好哦");
        }
    }

    private void updateView(int code, String content) {
        switch (code){
            case 0:
                showData(content);
                break;
            default:
                showData("机器人今天不工作！");
                break;
        }
    }

    private void showData(String s) {
        ChatBean chatBean = new ChatBean();
        chatBean.setMessage(s);
        chatBean.setState(ChatBean.RECEIVE);
        chatBeanList.add(chatBean);
        adapter.notifyDataSetChanged();
    }
long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - exitTime >2000){
                Toast.makeText(this,"再按一次退出智能聊天程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
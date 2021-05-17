package cn.itcast.robotchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytest.ChatBean;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private List<ChatBean> chatBeanList;
    private LayoutInflater layoutInflater;

    public ChatAdapter(List<ChatBean> chatBeanList, Context context){
        this.chatBeanList = chatBeanList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {   //获取数据源数据的总行数
        return chatBeanList.size();
    }

    @Override
    public Object getItem(int i) {  //获取对应行的数据对象
        return chatBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {   //获取对应行的键
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {   //获取对应行的View
        View item;
        if(chatBeanList.get(i).getState() == ChatBean.SEND){//聊天人
            item = layoutInflater.inflate(R.layout.chatting_right_item, null);
        }else {//机器人
            item = layoutInflater.inflate(R.layout.chatting_left_item, null);
        }

        TextView textView = item.findViewById(R.id.tv_chat_content);
        textView.setText(chatBeanList.get(i).getMessage());
        return item;
    }

}

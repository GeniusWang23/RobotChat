package cn.itcast.robotchat;

public class ChatBean {
    public static final int SEND = 1;
    public static final int RECEIVE = 2;

    private String message;
    private int state;           //1---发送;2---接收

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

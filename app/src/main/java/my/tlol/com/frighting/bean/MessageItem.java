package my.tlol.com.frighting.bean;

/**
 * Created by tlol20 on 2017/6/3
 */
public class MessageItem {
    private String name,message,time,icon;

    @Override
    public String toString() {
        return "MessageItem{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public MessageItem(String name, String message, String time, String icon) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.icon = icon;
    }
}

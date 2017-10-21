package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/6/12
 */
public class Msg {
    /**
     * code : 101
     * text : 请先登录
     */

    private MsgEntity Msg;

    public static Msg objectFromData(String str) {

        return new Gson().fromJson(str, Msg.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public static class MsgEntity {
        private int code;
        private String text;

        public static MsgEntity objectFromData(String str) {

            return new Gson().fromJson(str, MsgEntity.class);
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
}

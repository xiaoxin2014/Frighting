package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/6/30.
 */
public class Register {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * smYzmId : 13
     * telephone : 15088138460
     */

    private SmYzmEntity SmYzm;

    public static Register objectFromData(String str) {

        return new Gson().fromJson(str, Register.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setSmYzm(SmYzmEntity smYzm) {
        this.SmYzm = smYzm;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public SmYzmEntity getSmYzm() {
        return SmYzm;
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

    public static class SmYzmEntity {
        private int smYzmId;
        private String telephone;

        public static SmYzmEntity objectFromData(String str) {

            return new Gson().fromJson(str, SmYzmEntity.class);
        }

        public void setSmYzmId(int smYzmId) {
            this.smYzmId = smYzmId;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getSmYzmId() {
            return smYzmId;
        }

        public String getTelephone() {
            return telephone;
        }
    }
}

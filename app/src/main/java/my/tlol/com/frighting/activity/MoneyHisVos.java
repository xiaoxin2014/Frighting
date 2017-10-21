package my.tlol.com.frighting.activity;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/22.
 */

public class MoneyHisVos {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * money : 2000
     * sendDate : 2017-07-21 17:40:42
     * userId : 6
     */

    private List<MoneyHisVosEntity> moneyHisVos;

    public static MoneyHisVos objectFromData(String str) {

        return new Gson().fromJson(str, MoneyHisVos.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setMoneyHisVos(List<MoneyHisVosEntity> moneyHisVos) {
        this.moneyHisVos = moneyHisVos;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<MoneyHisVosEntity> getMoneyHisVos() {
        return moneyHisVos;
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

    public static class MoneyHisVosEntity {
        private String money;
        private String sendDate;
        private int userId;

        public static MoneyHisVosEntity objectFromData(String str) {

            return new Gson().fromJson(str, MoneyHisVosEntity.class);
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getMoney() {
            return money;
        }

        public String getSendDate() {
            return sendDate;
        }

        public int getUserId() {
            return userId;
        }
    }
}

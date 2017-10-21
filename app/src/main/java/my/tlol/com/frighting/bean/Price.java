package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/22. */

public class Price {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * oilName : 98#
     * price : 8.45
     */

    private List<EveryOilEntity> everyOil;

    public static Price objectFromData(String str) {

        return new Gson().fromJson(str, Price.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setEveryOil(List<EveryOilEntity> everyOil) {
        this.everyOil = everyOil;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<EveryOilEntity> getEveryOil() {
        return everyOil;
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

    public static class EveryOilEntity {
        private String oilName;
        private String price;

        public static EveryOilEntity objectFromData(String str) {

            return new Gson().fromJson(str, EveryOilEntity.class);
        }

        public void setOilName(String oilName) {
            this.oilName = oilName;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOilName() {
            return oilName;
        }

        public String getPrice() {
            return price;
        }
    }
}

package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/16
 */

public class Message {


    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * daysMessage : 大家注意
     * mesId : 1
     */

    private List<DayMesVoEntity> dayMesVo;

    public static Message objectFromData(String str) {

        return new Gson().fromJson(str, Message.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setDayMesVo(List<DayMesVoEntity> dayMesVo) {
        this.dayMesVo = dayMesVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<DayMesVoEntity> getDayMesVo() {
        return dayMesVo;
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

    public static class DayMesVoEntity {
        private String daysMessage;
        private int mesId;

        public static DayMesVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, DayMesVoEntity.class);
        }

        public void setDaysMessage(String daysMessage) {
            this.daysMessage = daysMessage;
        }

        public void setMesId(int mesId) {
            this.mesId = mesId;
        }

        public String getDaysMessage() {
            return daysMessage;
        }

        public int getMesId() {
            return mesId;
        }
    }
}

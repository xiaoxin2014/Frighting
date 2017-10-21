package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/11
 */
public class TaoCan {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * activityId : 1
     * buyM : 1899
     * giveM : 2550
     * payM : 601
     * stage : 6
     */

    private List<ActivityEntity> activity;

    public static TaoCan objectFromData(String str) {

        return new Gson().fromJson(str, TaoCan.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setActivity(List<ActivityEntity> activity) {
        this.activity = activity;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<ActivityEntity> getActivity() {
        return activity;
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

    public static class ActivityEntity {
        private String activityId;
        private int buyM;
        private int giveM;
        private int payM;
        private int stage;

        public static ActivityEntity objectFromData(String str) {

            return new Gson().fromJson(str, ActivityEntity.class);
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public void setBuyM(int buyM) {
            this.buyM = buyM;
        }

        public void setGiveM(int giveM) {
            this.giveM = giveM;
        }

        public void setPayM(int payM) {
            this.payM = payM;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public String getActivityId() {
            return activityId;
        }

        public int getBuyM() {
            return buyM;
        }

        public int getGiveM() {
            return giveM;
        }

        public int getPayM() {
            return payM;
        }

        public int getStage() {
            return stage;
        }
    }
}

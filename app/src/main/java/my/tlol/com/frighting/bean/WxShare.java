package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/22.
 */

public class WxShare {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * wxContent : 石油搞活动    分为好几种  大家来围观
     * wxTitle : 全民加油大麦成
     * wxUrl : http://www.sinopecsales.com/website/data/gjfj/image/201704/e7be856bffcb445bb340def58efe8548.jpg
     */

    private WxShareVoEntity wxShareVo;

    public static WxShare objectFromData(String str) {

        return new Gson().fromJson(str, WxShare.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setWxShareVo(WxShareVoEntity wxShareVo) {
        this.wxShareVo = wxShareVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public WxShareVoEntity getWxShareVo() {
        return wxShareVo;
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

    public static class WxShareVoEntity {
        private String wxContent;
        private String wxTitle;
        private String wxUrl;

        public static WxShareVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, WxShareVoEntity.class);
        }

        public void setWxContent(String wxContent) {
            this.wxContent = wxContent;
        }

        public void setWxTitle(String wxTitle) {
            this.wxTitle = wxTitle;
        }

        public void setWxUrl(String wxUrl) {
            this.wxUrl = wxUrl;
        }

        public String getWxContent() {
            return wxContent;
        }

        public String getWxTitle() {
            return wxTitle;
        }

        public String getWxUrl() {
            return wxUrl;
        }
    }
}

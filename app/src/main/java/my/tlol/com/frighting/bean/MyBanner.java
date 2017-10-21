package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/8/30
 */

public class MyBanner {


    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * herfUrl : http://ivan88.baiduux.com/h5/42cf10c4-4c08-62e6-d589-c0d2a25cff4c.html
     * pImagName : 唯品会
     * pImgId : 18
     * pImgUrl : http://120.76.42.163:8080/jybUserFile/other/147501.jpg
     */

    private List<MainImgVoEntity> mainImgVo;

    public static MyBanner objectFromData(String str) {

        return new Gson().fromJson(str, MyBanner.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setMainImgVo(List<MainImgVoEntity> mainImgVo) {
        this.mainImgVo = mainImgVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<MainImgVoEntity> getMainImgVo() {
        return mainImgVo;
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

    public static class MainImgVoEntity {
        private String herfUrl;
        private String pImagName;
        private int pImgId;
        private String pImgUrl;

        public static MainImgVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, MainImgVoEntity.class);
        }

        public void setHerfUrl(String herfUrl) {
            this.herfUrl = herfUrl;
        }

        public void setPImagName(String pImagName) {
            this.pImagName = pImagName;
        }

        public void setPImgId(int pImgId) {
            this.pImgId = pImgId;
        }

        public void setPImgUrl(String pImgUrl) {
            this.pImgUrl = pImgUrl;
        }

        public String getHerfUrl() {
            return herfUrl;
        }

        public String getPImagName() {
            return pImagName;
        }

        public int getPImgId() {
            return pImgId;
        }

        public String getPImgUrl() {
            return pImgUrl;
        }
    }
}

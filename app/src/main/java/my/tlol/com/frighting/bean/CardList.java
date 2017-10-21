package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/17
 */

public class CardList {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * cardNum : 9234567891234567
     * flag : zgsy
     * gasCardId : 6
     * userName : 彩排好
     */

    private List<GasCardVoEntity> gasCardVo;

    public static CardList objectFromData(String str) {

        return new Gson().fromJson(str, CardList.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setGasCardVo(List<GasCardVoEntity> gasCardVo) {
        this.gasCardVo = gasCardVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<GasCardVoEntity> getGasCardVo() {
        return gasCardVo;
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

    public static class GasCardVoEntity {
        private String cardNum;
        private String flag;
        private int gasCardId;
        private String userName;

        public static GasCardVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, GasCardVoEntity.class);
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public void setGasCardId(int gasCardId) {
            this.gasCardId = gasCardId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCardNum() {
            return cardNum;
        }

        public String getFlag() {
            return flag;
        }

        public int getGasCardId() {
            return gasCardId;
        }

        public String getUserName() {
            return userName;
        }
    }
}

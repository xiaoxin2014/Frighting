package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/11.
 */
public class AddCar {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * cardNum : 9999999999999999
     * flag : zgsy
     */

    private TbGasCardEntity tbGasCard;

    public static AddCar objectFromData(String str) {

        return new Gson().fromJson(str, AddCar.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setTbGasCard(TbGasCardEntity tbGasCard) {
        this.tbGasCard = tbGasCard;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public TbGasCardEntity getTbGasCard() {
        return tbGasCard;
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

    public static class TbGasCardEntity {
        private String cardNum;
        private String flag;

        public static TbGasCardEntity objectFromData(String str) {

            return new Gson().fromJson(str, TbGasCardEntity.class);
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getCardNum() {
            return cardNum;
        }

        public String getFlag() {
            return flag;
        }
    }
}

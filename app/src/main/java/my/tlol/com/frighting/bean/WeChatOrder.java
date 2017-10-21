package my.tlol.com.frighting.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2017/10/21 0021
 */

public class WeChatOrder {

    /**
     * Msg : {"code":-1,"text":""}
     * UnifiedOrder : {"appid":"wxcd0959cc13aa4005","noncestr":"4COca8fK6WSSS1B2","package":"Sign=WXPay","partnerid":"1487812582","prepayid":"wx2017102114434695707ba6610529437054","sign":"18AD622087DE6BD724356BB3CB75C56CD1DBA98B6089FD8C8C9BEBED46CEB5B7","timestamp":1508568226}
     */

    private MsgBean Msg;
    private UnifiedOrderBean UnifiedOrder;

    public MsgBean getMsg() {
        return Msg;
    }

    public void setMsg(MsgBean Msg) {
        this.Msg = Msg;
    }

    public UnifiedOrderBean getUnifiedOrder() {
        return UnifiedOrder;
    }

    public void setUnifiedOrder(UnifiedOrderBean UnifiedOrder) {
        this.UnifiedOrder = UnifiedOrder;
    }

    public static class MsgBean {
        /**
         * code : -1
         * text :
         */

        private int code;
        private String text;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class UnifiedOrderBean {
        /**
         * appid : wxcd0959cc13aa4005
         * noncestr : 4COca8fK6WSSS1B2
         * package : Sign=WXPay
         * partnerid : 1487812582
         * prepayid : wx2017102114434695707ba6610529437054
         * sign : 18AD622087DE6BD724356BB3CB75C56CD1DBA98B6089FD8C8C9BEBED46CEB5B7
         * timestamp : 1508568226
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private int timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}

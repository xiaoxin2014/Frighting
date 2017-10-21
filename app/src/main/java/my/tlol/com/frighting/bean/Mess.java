package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/16.
 */

public class Mess {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * content : 我爱你
     * customerId : 2
     * status : Y
     * userId : 6
     */

    private List<CustomerVoEntity> customerVo;

    public static Mess objectFromData(String str) {

        return new Gson().fromJson(str, Mess.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setCustomerVo(List<CustomerVoEntity> customerVo) {
        this.customerVo = customerVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<CustomerVoEntity> getCustomerVo() {
        return customerVo;
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

    public static class CustomerVoEntity {
        private String content;
        private int customerId;
        private String status;
        private int userId;

        public static CustomerVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, CustomerVoEntity.class);
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getStatus() {
            return status;
        }

        public int getUserId() {
            return userId;
        }
    }
}

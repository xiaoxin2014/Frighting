package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/16.
 */

public class CartF {
    /**
     * lsprefix : 粤
     * lsnum : MH7101
     * carorg : guangdong
     * usercarid : 38678760
     * list : {"score":"0","address":"新港东路_广州市海珠区新港东路路段","illegalid":"10841371","price":"200","time":"2017-03-14 08:49:00","legalnum":"7027","content":"机动车违反禁止停车标志、禁止长时停车标志或禁止停车线指示的"}
     */

    private ResultEntity result;
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;

    public static CartF objectFromData(String str) {

        return new Gson().fromJson(str, CartF.class);
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public ResultEntity getResult() {
        return result;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public static class ResultEntity {
        private String lsprefix;
        private String lsnum;
        private String carorg;
        private String usercarid;
        /**
         * score : 0
         * address : 新港东路_广州市海珠区新港东路路段
         * illegalid : 10841371
         * price : 200
         * time : 2017-03-14 08:49:00
         * legalnum : 7027
         * content : 机动车违反禁止停车标志、禁止长时停车标志或禁止停车线指示的
         */

        private ListEntity list;

        public static ResultEntity objectFromData(String str) {

            return new Gson().fromJson(str, ResultEntity.class);
        }

        public void setLsprefix(String lsprefix) {
            this.lsprefix = lsprefix;
        }

        public void setLsnum(String lsnum) {
            this.lsnum = lsnum;
        }

        public void setCarorg(String carorg) {
            this.carorg = carorg;
        }

        public void setUsercarid(String usercarid) {
            this.usercarid = usercarid;
        }

        public void setList(ListEntity list) {
            this.list = list;
        }

        public String getLsprefix() {
            return lsprefix;
        }

        public String getLsnum() {
            return lsnum;
        }

        public String getCarorg() {
            return carorg;
        }

        public String getUsercarid() {
            return usercarid;
        }

        public ListEntity getList() {
            return list;
        }

        public static class ListEntity {
            private String score;
            private String address;
            private String illegalid;
            private String price;
            private String time;
            private String legalnum;
            private String content;

            public static ListEntity objectFromData(String str) {

                return new Gson().fromJson(str, ListEntity.class);
            }

            public void setScore(String score) {
                this.score = score;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setIllegalid(String illegalid) {
                this.illegalid = illegalid;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setLegalnum(String legalnum) {
                this.legalnum = legalnum;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getScore() {
                return score;
            }

            public String getAddress() {
                return address;
            }

            public String getIllegalid() {
                return illegalid;
            }

            public String getPrice() {
                return price;
            }

            public String getTime() {
                return time;
            }

            public String getLegalnum() {
                return legalnum;
            }

            public String getContent() {
                return content;
            }
        }
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
}

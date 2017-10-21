package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/11
 */
public class CarDetail {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * bPrice : 19.80
     * carImgUrl : https://img2.rrcimg.com/o_1bkllkit6625517466418223065057070.jpg?imageView/4/w/600/h/400
     * content : 这辆车从2013年7月上牌至今一共跑了8.86万公里。车子陪着我的这几年，表现一直非常可靠，从没给我掉过链子，底盘扎实，操控稳健，非常适合家庭代步使用，喜欢的话就快联系我吧，绝对物超所值。
     * detailUrl : https://img2.rrcimg.com/dist/pc/images/detail-report/bottom-12824074.png
     * param : 52万公里 | 2.4L自动
     * playImgUrl1 : https://img2.rrcimg.com/o_1bkllkit71239721573351555796932852.jpg?imageView/4/w/600/h/400
     * playImgUrl2 : https://img2.rrcimg.com/o_1bkllkit684537330521086165914147.jpg?imageView/4/w/600/h/400
     * playImgUrl3 : imageView/4/w/600/h/400
     * playImgUrl4 : imageView/4/w/600/h/400
     * playImgUrl5 : imageView/4/w/600/h/400
     * productData : 2017-07-10 17:40:42.0
     * referPrice : 10.5~20.45
     * sPrice : 26.03
     * title : 奔驰svo-990
     */

    private CarDetailVoEntity carDetailVo;

    public static CarDetail objectFromData(String str) {

        return new Gson().fromJson(str, CarDetail.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setCarDetailVo(CarDetailVoEntity carDetailVo) {
        this.carDetailVo = carDetailVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public CarDetailVoEntity getCarDetailVo() {
        return carDetailVo;
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

    public static class CarDetailVoEntity {
        private String bPrice;
        private String carImgUrl;
        private String content;
        private String detailUrl;
        private String param;
        private String playImgUrl1;
        private String playImgUrl2;
        private String playImgUrl3;
        private String playImgUrl4;
        private String playImgUrl5;
        private String productData;
        private String referPrice;
        private String sPrice;
        private String title;

        public static CarDetailVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, CarDetailVoEntity.class);
        }

        public void setBPrice(String bPrice) {
            this.bPrice = bPrice;
        }

        public void setCarImgUrl(String carImgUrl) {
            this.carImgUrl = carImgUrl;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public void setPlayImgUrl1(String playImgUrl1) {
            this.playImgUrl1 = playImgUrl1;
        }

        public void setPlayImgUrl2(String playImgUrl2) {
            this.playImgUrl2 = playImgUrl2;
        }

        public void setPlayImgUrl3(String playImgUrl3) {
            this.playImgUrl3 = playImgUrl3;
        }

        public void setPlayImgUrl4(String playImgUrl4) {
            this.playImgUrl4 = playImgUrl4;
        }

        public void setPlayImgUrl5(String playImgUrl5) {
            this.playImgUrl5 = playImgUrl5;
        }

        public void setProductData(String productData) {
            this.productData = productData;
        }

        public void setReferPrice(String referPrice) {
            this.referPrice = referPrice;
        }

        public void setSPrice(String sPrice) {
            this.sPrice = sPrice;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBPrice() {
            return bPrice;
        }

        public String getCarImgUrl() {
            return carImgUrl;
        }

        public String getContent() {
            return content;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public String getParam() {
            return param;
        }

        public String getPlayImgUrl1() {
            return playImgUrl1;
        }

        public String getPlayImgUrl2() {
            return playImgUrl2;
        }

        public String getPlayImgUrl3() {
            return playImgUrl3;
        }

        public String getPlayImgUrl4() {
            return playImgUrl4;
        }

        public String getPlayImgUrl5() {
            return playImgUrl5;
        }

        public String getProductData() {
            return productData;
        }

        public String getReferPrice() {
            return referPrice;
        }

        public String getSPrice() {
            return sPrice;
        }

        public String getTitle() {
            return title;
        }
    }
}

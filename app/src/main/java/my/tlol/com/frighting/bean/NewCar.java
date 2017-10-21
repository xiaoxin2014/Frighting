package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/7/11
 */
public class NewCar {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * bPrice : 19.80
     * carId : 1
     * carImgUrl : https://img2.rrcimg.com/o_1bkllkit6625517466418223065057070.jpg?imageView/4/w/600/h/400
     * param : 52万公里 | 2.4L自动
     * productData : 2017-07-10 17:40:42.0
     * title : 奔驰svo-990
     */

    private List<CarMainVoEntity> carMainVo;

    public static NewCar objectFromData(String str) {

        return new Gson().fromJson(str, NewCar.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setCarMainVo(List<CarMainVoEntity> carMainVo) {
        this.carMainVo = carMainVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public List<CarMainVoEntity> getCarMainVo() {
        return carMainVo;
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

    public static class CarMainVoEntity {
        private String bPrice;
        private String carId;
        private String carImgUrl;
        private String param;
        private String productData;
        private String title;

        public static CarMainVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, CarMainVoEntity.class);
        }

        public void setBPrice(String bPrice) {
            this.bPrice = bPrice;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public void setCarImgUrl(String carImgUrl) {
            this.carImgUrl = carImgUrl;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public void setProductData(String productData) {
            this.productData = productData;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBPrice() {
            return bPrice;
        }

        public String getCarId() {
            return carId;
        }

        public String getCarImgUrl() {
            return carImgUrl;
        }

        public String getParam() {
            return param;
        }

        public String getProductData() {
            return productData;
        }

        public String getTitle() {
            return title;
        }
    }
}

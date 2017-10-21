package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/16.
 */

public class SGetCode {
    /**
     * msg : 执行成功!
     * code : 0
     * rtnImgYzm : {"imgBase64":"iVBORw0KGgoAAAANSUhEUgAAAFAAAAAoCAIAAADmAupWAAAB80lEQVR42uXYvU0EMRAFYCc0QAd0QEJCQHgJEh0QESDquIwGCInpgpAKkEgJqIGAgEUrWYPHfn5jr327vtVEe76Tvhn/zNm9jv68PO1kuFFhPoJhefDX/T4IPeb8/VoG/kFy8N3lQxBApWHRZ/dz63gnZpPgrHa2pVQzOzD4uHj8nAJok+CsVrOZuoExGoYjYGCqzIur0QJzcW35mL4yO6fwHjM4O3XLilygxQNkSeWsxtoQzOxP0ZG4yPVa//iSymHSo83Bp3/gk5vTwSKl/XcskbWNjgfbtbW8qapGxwNY6qXLAmrAzNbNzGRmPAZHGo+lwCmzFVADBqvalWmt4KW0BwPzXRduvGrKi7uu1KbtOsznRmB83qaO5TxYvwTlNW1XGKBfMmd1tglhwfJ9T7B83wqMbQVa8kzCNrIVo8Afz1cymP45q+X/M/L9c702fg4Hfp0OUktu19jDa81g8o8hTodWlV1uWGvbEBxUOKghzgVf5DKzGWw1k1txNhdT1JsZbfISD+xPVi25kkEuyDOpCky2WX3uX/HUsP5a4b10N61e7ZXpGOcinpkd44OPrsIG8Nv3mY+jq7DEbz0FhVN6uylYbA3rFKwzC203rRWmoPcuffCJsIpjqWcKVnoOt0vBZhqPpdbCtjutghSM1lpm8b8tsdyRHizCGgAAAABJRU5ErkJggg==","id":5}
     */

    private String msg;
    private int code;
    /**
     * imgBase64 : iVBORw0KGgoAAAANSUhEUgAAAFAAAAAoCAIAAADmAupWAAAB80lEQVR42uXYvU0EMRAFYCc0QAd0QEJCQHgJEh0QESDquIwGCInpgpAKkEgJqIGAgEUrWYPHfn5jr327vtVEe76Tvhn/zNm9jv68PO1kuFFhPoJhefDX/T4IPeb8/VoG/kFy8N3lQxBApWHRZ/dz63gnZpPgrHa2pVQzOzD4uHj8nAJok+CsVrOZuoExGoYjYGCqzIur0QJzcW35mL4yO6fwHjM4O3XLilygxQNkSeWsxtoQzOxP0ZG4yPVa//iSymHSo83Bp3/gk5vTwSKl/XcskbWNjgfbtbW8qapGxwNY6qXLAmrAzNbNzGRmPAZHGo+lwCmzFVADBqvalWmt4KW0BwPzXRduvGrKi7uu1KbtOsznRmB83qaO5TxYvwTlNW1XGKBfMmd1tglhwfJ9T7B83wqMbQVa8kzCNrIVo8Afz1cymP45q+X/M/L9c702fg4Hfp0OUktu19jDa81g8o8hTodWlV1uWGvbEBxUOKghzgVf5DKzGWw1k1txNhdT1JsZbfISD+xPVi25kkEuyDOpCky2WX3uX/HUsP5a4b10N61e7ZXpGOcinpkd44OPrsIG8Nv3mY+jq7DEbz0FhVN6uylYbA3rFKwzC203rRWmoPcuffCJsIpjqWcKVnoOt0vBZhqPpdbCtjutghSM1lpm8b8tsdyRHizCGgAAAABJRU5ErkJggg==
     * id : 5
     */

    private RtnImgYzmEntity rtnImgYzm;

    public static SGetCode objectFromData(String str) {

        return new Gson().fromJson(str, SGetCode.class);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setRtnImgYzm(RtnImgYzmEntity rtnImgYzm) {
        this.rtnImgYzm = rtnImgYzm;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public RtnImgYzmEntity getRtnImgYzm() {
        return rtnImgYzm;
    }

    public static class RtnImgYzmEntity {
        private String imgBase64;
        private int id;

        public static RtnImgYzmEntity objectFromData(String str) {

            return new Gson().fromJson(str, RtnImgYzmEntity.class);
        }

        public void setImgBase64(String imgBase64) {
            this.imgBase64 = imgBase64;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgBase64() {
            return imgBase64;
        }

        public int getId() {
            return id;
        }
    }
}

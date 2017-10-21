package my.tlol.com.frighting.http;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/21.
 */

public class UnifiedOrderBean {

    /**
     * appid : wxf32d800355649b09
     * noncestr : m6yqvTparHOwbQpY
     * package : Sign=WXPay
     * partnerid : 1426848502
     * prepayid : wx201710201140044feffbe5320120837508
     * sign : A6098E4E7CD0FBCFC870B88F3034EA9F2CB13E1695B4BC4CDD45F5F3B2B2C913
     * timestamp : 1508470619
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

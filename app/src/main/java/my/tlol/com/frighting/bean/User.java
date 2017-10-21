
package my.tlol.com.frighting.bean;


import com.google.gson.Gson;

public class User {


    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * award : 0
     * photoUrl : http://120.76.42.163:8080/jybUserFile/userPhoto/Mo.jpg
     * role : 普通用户
     * share : 0
     * telephone : 15088138460
     * userId : 6
     * wtSave : 0
     */
    private UserVoLogEntity userVoLog;

    public static User objectFromData(String str) {
        return new Gson().fromJson(str, User.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setUserVoLog(UserVoLogEntity userVoLog) {
        this.userVoLog = userVoLog;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public UserVoLogEntity getUserVoLog() {
        return userVoLog;
    }

    public Class<UserVoLogEntity> setUserVoLog(Class<UserVoLogEntity> userVoLogEntityClass) {
        return userVoLogEntityClass;
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

    public static class UserVoLogEntity {
        private String award;
        private String photoUrl;
        private String role;
        private String share;
        private String telephone;
        private int userId;
        private String wtSave;

        public static UserVoLogEntity objectFromData(String str) {

            return new Gson().fromJson(str, UserVoLogEntity.class);
        }

        public void setAward(String award) {
            this.award = award;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setWtSave(String wtSave) {
            this.wtSave = wtSave;
        }

        public String getAward() {
            return award;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public String getRole() {
            return role;
        }

        public String getShare() {
            return share;
        }

        public String getTelephone() {
            return telephone;
        }

        public int getUserId() {
            return userId;
        }

        public String getWtSave() {
            return wtSave;
        }
    }
}

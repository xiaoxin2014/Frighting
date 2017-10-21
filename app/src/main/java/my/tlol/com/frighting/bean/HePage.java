package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by tlol20 on 2017/6/15
 */
public class HePage {
    /**
     * code : 0
     * text : 请求成功
     */

    private MsgEntity Msg;
    /**
     * address :
     * birthday : 1987-11-03
     * browseByTotal : 2
     * favoByTotal : 3
     * focusByTotal : 0
     * focusTotal : 0
     * focused : N
     * friendinged : N
     * loginTime : 2017-06-12 18:54:28.446
     * nickName : 一个人
     * personSign : 自信来自实力
     * photoUrl : http://192.168.0.110:8080/cfUserFile/userPhoto/18634726593_5484.jpg
     * sex : 男
     * upvoteByTotal : 1
     * upvoted : N
     * userId : 96
     */

    private UserVoOthEntity UserVoOth;
    /**
     * authNickName : 一个人
     * authPhotoUrl : http://192.168.0.110:8080/cfUserFile/userPhoto/18634726593_5484.jpg
     * authUserId : 96
     * cateSqn : 0
     * category : 美食
     * content1 : 这是动态内容1
     * noteId : 120
     * noteMmxUrl1 : http://192.168.0.112:8080/cfUserFile/noteFile/mmx_803b5da85ed54c6eafa29e224e9dc060.jpg
     * noteSqn : 0
     * readByTotal : 1
     * title1 : 这是动态标题1
     */

    private List<NoteVoListEntity> NoteVoList;
    /**
     * browseDate : 2017-06-11 18:42:50.346
     * browseUserId : 96
     * photoUrl : http://192.168.0.112:8080/cfUserFile/userPhoto/13524846234_5388.jpg
     * userId : 23
     */

    private List<UserBrowseVoEntity> UserBrowseVo;

    public static HePage objectFromData(String str) {

        return new Gson().fromJson(str, HePage.class);
    }

    public void setMsg(MsgEntity msg) {
        this.Msg = msg;
    }

    public void setUserVoOth(UserVoOthEntity userVoOth) {
        this.UserVoOth = userVoOth;
    }

    public void setNoteVoList(List<NoteVoListEntity> noteVoList) {
        this.NoteVoList = noteVoList;
    }

    public void setUserBrowseVo(List<UserBrowseVoEntity> userBrowseVo) {
        this.UserBrowseVo = userBrowseVo;
    }

    public MsgEntity getMsg() {
        return Msg;
    }

    public UserVoOthEntity getUserVoOth() {
        return UserVoOth;
    }

    public List<NoteVoListEntity> getNoteVoList() {
        return NoteVoList;
    }

    public List<UserBrowseVoEntity> getUserBrowseVo() {
        return UserBrowseVo;
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

    public static class UserVoOthEntity {
        private String address;
        private String birthday;
        private int browseByTotal;
        private int favoByTotal;
        private int focusByTotal;
        private int focusTotal;
        private String focused;
        private String friendinged;
        private String loginTime;
        private String nickName;
        private String personSign;
        private String photoUrl;
        private String sex;
        private int upvoteByTotal;
        private String upvoted;
        private int userId;

        public static UserVoOthEntity objectFromData(String str) {

            return new Gson().fromJson(str, UserVoOthEntity.class);
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setBrowseByTotal(int browseByTotal) {
            this.browseByTotal = browseByTotal;
        }

        public void setFavoByTotal(int favoByTotal) {
            this.favoByTotal = favoByTotal;
        }

        public void setFocusByTotal(int focusByTotal) {
            this.focusByTotal = focusByTotal;
        }

        public void setFocusTotal(int focusTotal) {
            this.focusTotal = focusTotal;
        }

        public void setFocused(String focused) {
            this.focused = focused;
        }

        public void setFriendinged(String friendinged) {
            this.friendinged = friendinged;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setPersonSign(String personSign) {
            this.personSign = personSign;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setUpvoteByTotal(int upvoteByTotal) {
            this.upvoteByTotal = upvoteByTotal;
        }

        public void setUpvoted(String upvoted) {
            this.upvoted = upvoted;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getAddress() {
            return address;
        }

        public String getBirthday() {
            return birthday;
        }

        public int getBrowseByTotal() {
            return browseByTotal;
        }

        public int getFavoByTotal() {
            return favoByTotal;
        }

        public int getFocusByTotal() {
            return focusByTotal;
        }

        public int getFocusTotal() {
            return focusTotal;
        }

        public String getFocused() {
            return focused;
        }

        public String getFriendinged() {
            return friendinged;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public String getNickName() {
            return nickName;
        }

        public String getPersonSign() {
            return personSign;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public String getSex() {
            return sex;
        }

        public int getUpvoteByTotal() {
            return upvoteByTotal;
        }

        public String getUpvoted() {
            return upvoted;
        }

        public int getUserId() {
            return userId;
        }
    }

    public static class NoteVoListEntity {
        private String authNickName;
        private String authPhotoUrl;
        private int authUserId;
        private int cateSqn;
        private String category;
        private String content1;
        private int noteId;
        private String noteMmxUrl1;
        private int noteSqn;
        private int readByTotal;
        private String title1;

        public static NoteVoListEntity objectFromData(String str) {

            return new Gson().fromJson(str, NoteVoListEntity.class);
        }

        public void setAuthNickName(String authNickName) {
            this.authNickName = authNickName;
        }

        public void setAuthPhotoUrl(String authPhotoUrl) {
            this.authPhotoUrl = authPhotoUrl;
        }

        public void setAuthUserId(int authUserId) {
            this.authUserId = authUserId;
        }

        public void setCateSqn(int cateSqn) {
            this.cateSqn = cateSqn;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setContent1(String content1) {
            this.content1 = content1;
        }

        public void setNoteId(int noteId) {
            this.noteId = noteId;
        }

        public void setNoteMmxUrl1(String noteMmxUrl1) {
            this.noteMmxUrl1 = noteMmxUrl1;
        }

        public void setNoteSqn(int noteSqn) {
            this.noteSqn = noteSqn;
        }

        public void setReadByTotal(int readByTotal) {
            this.readByTotal = readByTotal;
        }

        public void setTitle1(String title1) {
            this.title1 = title1;
        }

        public String getAuthNickName() {
            return authNickName;
        }

        public String getAuthPhotoUrl() {
            return authPhotoUrl;
        }

        public int getAuthUserId() {
            return authUserId;
        }

        public int getCateSqn() {
            return cateSqn;
        }

        public String getCategory() {
            return category;
        }

        public String getContent1() {
            return content1;
        }

        public int getNoteId() {
            return noteId;
        }

        public String getNoteMmxUrl1() {
            return noteMmxUrl1;
        }

        public int getNoteSqn() {
            return noteSqn;
        }

        public int getReadByTotal() {
            return readByTotal;
        }

        public String getTitle1() {
            return title1;
        }
    }

    public static class UserBrowseVoEntity {
        private String browseDate;
        private int browseUserId;
        private String photoUrl;
        private int userId;

        public static UserBrowseVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, UserBrowseVoEntity.class);
        }

        public void setBrowseDate(String browseDate) {
            this.browseDate = browseDate;
        }

        public void setBrowseUserId(int browseUserId) {
            this.browseUserId = browseUserId;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getBrowseDate() {
            return browseDate;
        }

        public int getBrowseUserId() {
            return browseUserId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public int getUserId() {
            return userId;
        }
    }
}

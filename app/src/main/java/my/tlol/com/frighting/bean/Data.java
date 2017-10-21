package my.tlol.com.frighting.bean;

import com.google.gson.Gson;

/**
 * Created by tlol20 on 2017/7/16.
 */

public class Data {
    /**
     * endDay : 2017-07-13 15:10:21.0
     * every : 100
     * money : 100000
     * people : 7000
     * startDay : 2017-07-13 15:10:18.0
     */

    private JybDataVoEntity jybDataVo;

    public static Data objectFromData(String str) {

        return new Gson().fromJson(str, Data.class);
    }

    public void setJybDataVo(JybDataVoEntity jybDataVo) {
        this.jybDataVo = jybDataVo;
    }

    public JybDataVoEntity getJybDataVo() {
        return jybDataVo;
    }

    public static class JybDataVoEntity {
        private String endDay;
        private String every;
        private String money;
        private String people;
        private String startDay;

        public static JybDataVoEntity objectFromData(String str) {

            return new Gson().fromJson(str, JybDataVoEntity.class);
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public void setEvery(String every) {
            this.every = every;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setPeople(String people) {
            this.people = people;
        }

        public void setStartDay(String startDay) {
            this.startDay = startDay;
        }

        public String getEndDay() {
            return endDay;
        }

        public String getEvery() {
            return every;
        }

        public String getMoney() {
            return money;
        }

        public String getPeople() {
            return people;
        }

        public String getStartDay() {
            return startDay;
        }
    }
}

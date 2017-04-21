package com.wulei.runner.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wule on 2017/04/21.
 */

public class User extends BmobUser {
    private BmobFile icon;
    private Integer age;
    private Boolean sex;
    private String singedText;

    public String getSinedText() {
        return singedText;
    }

    public void setSinedText(String sinedText) {
        this.singedText = sinedText;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}

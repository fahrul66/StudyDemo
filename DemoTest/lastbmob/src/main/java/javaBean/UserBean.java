package javaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wulei on 2017/1/10.
 */

public class UserBean extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

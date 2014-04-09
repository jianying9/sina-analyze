package com.wolf.sina.spider.localservice;

/**
 *
 * @author jianying9
 */
public final class InfoEntity {

    private final String userId;
    private String nickName = "";
    private String location = "";
    private String gender = "";
    private String empName = "";
    private String tag = "";

    public InfoEntity(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

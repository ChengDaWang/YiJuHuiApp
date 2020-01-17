package com.example.yipartyapp;

public class User {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;

    /**
     *新用户标记
     */
    private String newUserFlag;

    public User(String userName, String passWord,String newUserFlag) {
        this.userName = userName;
        this.passWord = passWord;
        this.newUserFlag=newUserFlag;
    }

    public String getNewUserFlag() {
        return newUserFlag;
    }

    public void setNewUserFlag(String newUserFlag) {
        this.newUserFlag = newUserFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * 转String类型，确保校验通过
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", newUserFlag='" + newUserFlag + '\'' +
                '}';
    }
}

package com.example.yipartyapp.bean;

public class ID {
    private String school;
    private String adress;
    private String beginData;
    private String endData;
    private String job;

    public ID(String school, String adress, String beginData, String endData, String job) {
        this.school = school;
        this.adress = adress;
        this.beginData = beginData;
        this.endData = endData;
        this.job = job;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBeginData() {
        return beginData;
    }

    public void setBeginData(String beginData) {
        this.beginData = beginData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "ID{" +
                "school='" + school + '\'' +
                ", adress='" + adress + '\'' +
                ", beginData='" + beginData + '\'' +
                ", endData='" + endData + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}

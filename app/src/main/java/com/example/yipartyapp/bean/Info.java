package com.example.yipartyapp.bean;

public class Info {
    private String name;
    private  String school;

    public Info(String name, String school) {
        this.name=name;
        this.school=school;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", school='" + school + '\'' +
                '}';
    }
}

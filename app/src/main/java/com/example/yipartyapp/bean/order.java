package com.example.yipartyapp.bean;

public class order {
    private String name;
    private String money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "order{" +
                "name='" + name + '\'' +
                ", money='" + money + '\'' +
                '}';
    }

    public order(String name, String money) {
        this.name = name;
        this.money = money;
    }
}

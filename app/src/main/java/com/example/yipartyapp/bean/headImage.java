package com.example.yipartyapp.bean;

/**
 * 用于头像加载
 */
public class headImage {
    private String headImage;

    public headImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Override
    public String toString() {
        return "headImage{" +
                "headImage='" + headImage + '\'' +
                '}';
    }
}

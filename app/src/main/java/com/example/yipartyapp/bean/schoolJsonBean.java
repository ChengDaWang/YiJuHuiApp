package com.example.yipartyapp.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class schoolJsonBean implements IPickerViewData {
    /**
     * 中国大学Json数据（2019）
     * 举例
     * province_name  省份
     * cities [{"city_name":"北京市","universities":["北京大学","中国人民大学","清华大学"]}]
     */
    private String province_name;
    private List<Universities> cities;

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public List<Universities> getCities() {
        return cities;
    }

    public void setCities(List<Universities> cities) {
        this.cities = cities;
    }

    @Override
    public String getPickerViewText() {
        return this.province_name;
    }

    public static class Universities{
        private String city_name;
        private List<String> universities;

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<String> getUniversities() {
            return universities;
        }

        public void setUniversities(List<String> universities) {
            this.universities = universities;
        }
    }
}

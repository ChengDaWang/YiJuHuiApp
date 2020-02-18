package com.example.yipartyapp.core.MinePage_headImage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.MainActivity;
import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.GetJsonDataUtil;
import com.example.yipartyapp.bean.schoolJsonBean;
import com.example.yipartyapp.core.MinePage;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SchoolActivity extends AppCompatActivity {
    private TextView textView2;//学校
    private DBOpenHelper mDBOpenHelper;
    private OptionsPickerView pvOptions1;//学校
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        mDBOpenHelper = new DBOpenHelper(this);
        textView2=findViewById(R.id.school);
        initSchoolPicker();
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.school:
                if(pvOptions1 != null){
                    pvOptions1.show();
                }
                break;
        }
    }

    public void initSchoolPicker(){
        initSchoolJsonData();
        showSchoolPickerView();
    }
    private List<schoolJsonBean> options1Items1 = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items1 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items1 = new ArrayList<>();

    private void initSchoolJsonData() {//解析数据
        String JsonData1 = new GetJsonDataUtil().getJson(this,"school");//获取json文件数据
        ArrayList<schoolJsonBean> schoolJsonBean = parseData1(JsonData1);//用Gson 转成实体

        options1Items1 = schoolJsonBean;

        for (int i = 0; i < schoolJsonBean.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < schoolJsonBean.get(i).getCities().size(); c++) {//遍历该省份的所有城市
                String cityName = schoolJsonBean.get(i).getCities().get(c).getCity_name();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();
                city_AreaList.addAll(schoolJsonBean.get(i).getCities().get(c).getUniversities());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items1.add(cityList);

            /**
             * 添加学校数据
             */
            options3Items1.add(province_AreaList);
        }
    }
    private ArrayList<schoolJsonBean> parseData1(String result) {//Gson 解析
        ArrayList<schoolJsonBean> detail1 = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                schoolJsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), schoolJsonBean.class);
                detail1.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail1;
    }
    private void showSchoolPickerView() {// 弹出选择器
        pvOptions1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items1.size() > 0 ?
                        options1Items1.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items1.size() > 0
                        && options2Items1.get(options1).size() > 0 ?
                        options2Items1.get(options1).get(options2) : "";

                String opt3tx = options2Items1.size() > 0
                        && options3Items1.get(options1).size() > 0
                        && options3Items1.get(options1).get(options2).size() > 0 ?
                        options3Items1.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                textView2.setText(tx);//将选择中的数据带回textView中显示
            }
        })
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .build();
        pvOptions1.setPicker(options1Items1, options2Items1, options3Items1);//三级选择器
    }
    public void check(View view){
        String school=textView2.getText().toString().trim();
        if(school!="请选择您的大学") {
            mDBOpenHelper.updataschool(school);
            Intent intent = new Intent(this, MinePage.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "学校信息为输入完整！", Toast.LENGTH_SHORT).show();
        }
    }
    public void returnLost(View view){
        Intent intent = new Intent(this, MineInfoActivity.class);
        startActivity(intent);
        finish();
    }
}

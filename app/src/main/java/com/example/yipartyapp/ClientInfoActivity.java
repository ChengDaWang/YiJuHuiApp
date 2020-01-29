package com.example.yipartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.example.yipartyapp.Utils.GetJsonDataUtil;
import com.example.yipartyapp.bean.JsonBean;
import com.example.yipartyapp.bean.schoolJsonBean;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;//时间
    private TextView textView1;//地址
    private TextView textView2;//学校
    private OptionsPickerView pvOptions;//地址
    private OptionsPickerView pvOptions1;//学校
    private TimePickerView pvTime;
    private EditText clientName;//真实姓名
    private MyDialog mMyDialog;
    private DBOpenHelper mDBOpenHelper;
    private Spinner gender1;
    private String gender;
    private String homeTown;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_client_info);

        mDBOpenHelper = new DBOpenHelper(this);
        /**
         * 时间选择
         */
        textView = findViewById(R.id.bornData);
        textView.setOnClickListener(this);
        /**
         * 地址选择
         */
        textView1=findViewById(R.id.bornAdress);
        textView1.setOnClickListener(this);
        /**
         * 学校选择
         */
        textView2=findViewById(R.id.school);
        textView2.setOnClickListener(this);

            initTimePicker();//时间选择
            initCityPicker();//地址选择
            initSchoolPicker();//学校选择
        /**
         * 真实姓名校验—设值
         */
        clientName=findViewById(R.id.clientName);
        /**
         * 获取Spinner中的当前值
         */
        gender1=(Spinner)findViewById(R.id.gender);
        gender = (String) gender1.getSelectedItem();

    }
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bornAdress:

                if (pvOptions != null) {
                    pvOptions.show(view);//弹出城市选择器，传递参数过去，回调的时候则可以绑定此view
                }
                break;
            case R.id.bornData:
                if (pvTime != null) {
                    pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
                }
                break;
            case R.id.school:
                if(pvOptions1 != null){
                    pvOptions1.show();
                }
                break;
        }
    }
    /**
     * 出生日期联动选择
     */
    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                textView.setText(getTime(date));
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 出生地址联动选择
     */
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    private void initCityPicker() {
            initJsonData();//解析Json数据方法
            showPickerView();//弹出选择框方法
    }

    private void initSchoolPicker(){
        initSchoolJsonData();
        showSchoolPickerView();
    }

    private void showPickerView() {// 弹出选择器
            pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String opt1tx = options1Items.size() > 0 ?
                                options1Items.get(options1).getPickerViewText() : "";

                        String opt2tx = options2Items.size() > 0
                                && options2Items.get(options1).size() > 0 ?
                                options2Items.get(options1).get(options2) : "";

                        String opt3tx = options2Items.size() > 0
                                && options3Items.get(options1).size() > 0
                                && options3Items.get(options1).get(options2).size() > 0 ?
                                options3Items.get(options1).get(options2).get(options3) : "";

                        String tx = opt1tx + opt2tx + opt3tx;
                        textView1.setText(tx);//将选择中的数据带回textView中显示
                    }
            })


                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器

        }


    private void initJsonData() {//解析数据
        String JsonData = new GetJsonDataUtil().getJson(this, "province");//获取json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }
    private ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
    /**
     * 大学选择器
     */
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

    /**
     * 点击“确认提交”按钮事件
     * 校验用户输入的数据，并入库
     */
    public void Check(View view) throws ParseException {
        String realName=clientName.getText().toString().trim();
        String gender= (String) gender1.getSelectedItem();
        String bornData=textView.getText().toString().trim();
        String homeTown=textView1.getText().toString().trim();
        String school=textView2.getText().toString().trim();
        String regEx ="^[\\u0391-\\uFFE5]{2,5}";//正则表达式，规则（仅限汉字，并且长度只能为2-5个汉字）
        String newUserFlag="no";//成功执行过此注册方法后的用户将被更新为老用户
        Pattern pattern = Pattern.compile(regEx);//
        Matcher matcher = pattern.matcher(realName);
        boolean rs = matcher.matches();
        //获取当前时间
        if(!bornData.equals("请输入出生日期")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date bornData1 = format.parse(bornData);
            Date date = new Date(System.currentTimeMillis());

        //String currData=format.format(date);
        if((homeTown.equals("请选择您的家乡") && school.equals("请选择您的大学")) ||(TextUtils.isEmpty(bornData) || bornData1.getTime() > date.getTime()) || (rs==false)){
            view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
            mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
        }else{
            mDBOpenHelper.addRealName(realName,gender,bornData,homeTown,school);
            mDBOpenHelper.updata(newUserFlag);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }else {
            view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
            mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
            mMyDialog.setCancelable(true);
            mMyDialog.show();
        }
    }
    /**
     * 弹窗关闭
     * @param view
     */
    public void close(View view){
        mMyDialog.cancel();
    }
}

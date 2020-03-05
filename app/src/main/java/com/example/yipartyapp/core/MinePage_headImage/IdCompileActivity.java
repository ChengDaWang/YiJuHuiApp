package com.example.yipartyapp.core.MinePage_headImage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.MyDialog;
import com.example.yipartyapp.R;
import com.example.yipartyapp.RegisterActivity;
import com.example.yipartyapp.Utils.GetJsonDataUtil;
import com.example.yipartyapp.bean.schoolJsonBean;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IdCompileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView schoolText,beginText,endText;
    private EditText adressText,jobText;
    private DBOpenHelper mDBOpenHelper;
    private OptionsPickerView pvOptions1;//学校
    private TimePickerView pvTime;
    private TimePickerView pvTime1;
    private MyDialog mMyDialog;
    private ListView mLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_compile);
        mDBOpenHelper = new DBOpenHelper(this);
        schoolText=findViewById(R.id.editText9);
        schoolText.setOnClickListener(this);
        beginText=findViewById(R.id.editText5);
        beginText.setOnClickListener(this);
        endText=findViewById(R.id.editText6);
        endText.setOnClickListener(this);
        adressText=findViewById(R.id.editText);
        jobText=findViewById(R.id.editText7);
        mDBOpenHelper = new DBOpenHelper(this);
        initTimePicker();
        initTimePicker1();
        initSchoolPicker();
    }

    private void initSchoolPicker(){
        initSchoolJsonData();
        showSchoolPickerView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editText9:
                if(pvOptions1 != null){
                    pvOptions1.show();
                }
                break;

            case R.id.editText5:
                if (pvTime != null) {
                    pvTime.show(view);//开始时间
                }
                break;

            case R.id.editText6:
                if (pvTime1 != null) {
                    pvTime1.show(view);//结束时间
                }
                break;
        }
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                beginText.setText(getTime(date));
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

    private void initTimePicker1() {//Dialog 模式下，在底部弹出
        pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                endText.setText(getTime1(date));
                Log.i("pvTime1", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime1", "onTimeSelectChanged");
                    }
                })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime1.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime1.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    private String getTime1(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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

                String tx = opt3tx;
                schoolText.setText(tx);//将选择中的数据带回textView中显示
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
    public void close(View view){
        mMyDialog.cancel();
    }

    public void sure(View view){
        String schoolInput = schoolText.getText().toString().trim();
        String adressInput = adressText.getText().toString().trim();
        String beginInput = beginText.getText().toString().trim();
        String endInput = endText.getText().toString().trim();
        String jobInput = jobText.getText().toString().trim();
        if(!TextUtils.isEmpty(schoolInput) && !TextUtils.isEmpty(adressInput)
        && !TextUtils.isEmpty(beginInput) && !TextUtils.isEmpty(endInput)
        && !TextUtils.isEmpty(jobInput)){
           int res=beginInput.compareTo(endInput);
           if(res<0){
            mDBOpenHelper.addID(schoolInput,adressInput,beginInput,endInput,jobInput);
               Intent intent=new Intent(this, IdentityActivity.class);
               startActivity(intent);
               finish();
               Toast.makeText(this, "成功添加身份信息", Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
           }
        }else {
            Toast.makeText(this, "输入不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
        }
    }
}

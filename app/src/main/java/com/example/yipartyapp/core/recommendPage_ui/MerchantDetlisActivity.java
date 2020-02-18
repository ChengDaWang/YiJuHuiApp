package com.example.yipartyapp.core.recommendPage_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.MainActivity;
import com.example.yipartyapp.R;

public class MerchantDetlisActivity extends AppCompatActivity {

    private TextView merchantName,merchantAdress,merchantMoney;
    private DBOpenHelper mDbopenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detlis);
        //实例化数据库
        mDbopenHelper = new DBOpenHelper(this);

        Intent detils = getIntent();
        /**
         * 获取商家名称
         */
        merchantName=(TextView)findViewById(R.id.textView35);
        merchantName.setText(detils.getStringExtra("merchantName"));
        /**
         * 获取商家地址
         */
        merchantAdress=(TextView)findViewById(R.id.textView39);
        merchantAdress.setText(detils.getStringExtra("merchantAdress"));
        /**
         * 获取价格
         */
        merchantMoney=(TextView)findViewById(R.id.textView38);
        merchantMoney.setText(detils.getStringExtra("merchantMoney"));
    }

    /**
     * 返回功能
     */
    public void back(View view){
        Intent intent = new Intent(this, MerchantFragment.class);
        startActivity(intent);
        finish();
    }

    /**
     * 加入订单
     */
    public void addOrder(View view){
        String mName=merchantName.getText().toString().trim();
        String mPrice=merchantMoney.getText().toString().trim();
        if(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice)){
            mDbopenHelper.addOrderDetails(mName,mPrice);
            Toast.makeText(this, "您已成功添加该订单！", Toast.LENGTH_SHORT).show();
        }

    }
}

package com.example.yipartyapp.core.MinePage_headImage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.LoginActivity;
import com.example.yipartyapp.R;
import com.example.yipartyapp.User;
import com.example.yipartyapp.core.MinePage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UpPasswordActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;
    private EditText oldPassword;//旧密码
    private EditText newPassword;//新密码
    private EditText surePassword;//确认密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_password);

        mDBOpenHelper= new DBOpenHelper(this);
        oldPassword=findViewById(R.id.editText2);
        newPassword=findViewById(R.id.editText3);
        surePassword=findViewById(R.id.editText4);
    }

    /**
     * 提交检查
     */
    public void checkPassword(View view){
        String op=oldPassword.getText().toString().trim();
        String np=newPassword.getText().toString().trim();
        String pasword=surePassword.getText().toString().trim();
        if(!TextUtils.isEmpty(op) && !TextUtils.isEmpty(np) && !TextUtils.isEmpty(pasword)){
            ArrayList<User> data=mDBOpenHelper.getAllData();
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(op.getBytes());
                String result = "";
                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                    op=result;//这里密码已经加密完成
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            boolean match=false;//设定一个判断标记
            //遍历数据库中存入的密码
            for(int i=0;i < data.size();i++){
                User user=data.get(i);
                if(op.equals(user.getPassWord())){
                    if(np.equals(pasword)){
                        //新密码加密处理后再存入数据库
                        MessageDigest md6 = null;
                        try {
                            md6 = MessageDigest.getInstance("MD5");
                            byte[] bytes = md6.digest(pasword.getBytes());
                            String result = "";
                            for (byte b : bytes) {
                                String temp = Integer.toHexString(b & 0xff);
                                if (temp.length() == 1) {
                                    temp = "0" + temp;
                                }
                                result += temp;
                                pasword=result;//这里密码已经加密完成
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        mDBOpenHelper.upPassword(pasword);
                        Intent intent=new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "修改成功,请重新登陆", Toast.LENGTH_SHORT).show();
                        /*reStartApp();*/   //重启虽然功能上更合理，但是效果不佳，所以放弃重启方式

                   }else {
                        Toast.makeText(this, "两次密码输入不正确", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "旧密码输入不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 重启app
     */
    public void reStartApp(){
        Intent intent = getBaseContext()
                .getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10,restartIntent);
        System.exit(0);
    }
}

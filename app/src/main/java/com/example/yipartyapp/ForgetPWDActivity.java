package com.example.yipartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.OperationCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPWDActivity extends AppCompatActivity {

    private EditText phoneInput,newPWInput,surePWInput,yzmInput;
    private String phone;
    private String newpassword;
    private String surepassword;
    private DBOpenHelper mDBOpenHelper;
    private String number;
    private Button button;
    private int recLen = 60;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        submitPrivacyGrantResult(true);

        phoneInput=findViewById(R.id.editText5);
        newPWInput=findViewById(R.id.editText6);
        surePWInput=findViewById(R.id.editText7);
        yzmInput=findViewById(R.id.editText8);
        button=findViewById(R.id.button8);


        mDBOpenHelper = new DBOpenHelper(this);

        EventHandler handler = new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE){

                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newpassword=newPWInput.getText().toString().trim();
                                surepassword=surePWInput.getText().toString().trim();
                                if(newpassword.equals(surepassword)) {
                                    MessageDigest md5 = null;
                                    try {
                                        md5 = MessageDigest.getInstance("MD5");
                                        byte[] bytes = md5.digest(surepassword.getBytes());
                                        String result = "";
                                        for (byte b : bytes) {
                                            String temp = Integer.toHexString(b & 0xff);
                                            if (temp.length() == 1) {
                                                temp = "0" + temp;
                                            }
                                            result += temp;
                                            surepassword = result;//这里密码已经加密完成
                                        }
                                        //将用户名和经过加密处理的密码存储到数据库中
                                        mDBOpenHelper.upPassword(surepassword);
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                    toLogin();
                                    Toast.makeText(ForgetPWDActivity.this, "设置新密码成功，请再次登录", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ForgetPWDActivity.this, "前后密码不一致，请再次输入", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgetPWDActivity.this,"语音验证发送",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ForgetPWDActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        Log.i("test","test");
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    Log.i("1234",throwable.toString());
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ForgetPWDActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(handler);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    button.setText("获取验证码"+"("+recLen+")");
                    if(recLen==0){
                        button.setText("重新获取");
                    }
                }
            });
        }
    };
    /**
     * 注意：该接口必须接入，否则可能造成无法使用MobTech各SDK提供的相关服务。
     * 国家规定的软件开发规范流程，保护用户隐私协议，此方法参考SMSSDK 技术文档
     * 网址（http://www.mob.com/wiki/detailed?wiki=SMSSDK_for_Android_kuaisujicheng&id=23）
     * @param granted
     */
    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
               // Log.d(TAG, "隐私协议授权结果提交：成功");
            }
            @Override
            public void onFailure(Throwable t) {
                //Log.d(TAG, "隐私协议授权结果提交：失败");
            }
        });
    }

    public void play(View view) {
        timer.schedule(task, 1000, 1000);
        phone=phoneInput.getText().toString().trim();
        //获取验证码
        SMSSDK.getVerificationCode("86",phone);
    }

    /**
     * 提交检查
     */
    public void tijiao(View view){
        phone=phoneInput.getText().toString().trim();
        number=yzmInput.getText().toString().trim();
        SMSSDK.submitVerificationCode("86",phone,number);
    }

    private void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

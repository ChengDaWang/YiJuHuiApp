package com.example.yipartyapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{
    /**
     * 声明DBOpenHelper 对象，目的---创建数据表，并对数据表进行操作
     */
    private DBOpenHelper mDBOpenHelper;
    /**
     * 昵称
     */
    private EditText regiesterName;
    /**
     * 第一次输入密码
     */
    private EditText regiesterPassword;
    /**
     * 第二次输入密码
     */
    private EditText regiesterSurePassWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
    }

    private void initView() {
         regiesterName=findViewById(R.id.regiesName);
         regiesterPassword=findViewById(R.id.editText2);
         regiesterSurePassWord=findViewById(R.id.regpassword);
    }

    //返回事件
    public void back(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //注册事件(以下代码块注释参考LoginActivity，这里不做解释)
    public void regiester(View view){
            String userName = regiesterName.getText().toString().trim();
            String passWord = regiesterPassword.getText().toString().trim();
            String passWord1 = regiesterSurePassWord.getText().toString().trim();
            if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(passWord1)){

                if(passWord.equals(passWord1)){
                    if(passWord.length()>=6 && passWord.length()<=10) {
                        //将用户名和密码加入到数据库中
                        mDBOpenHelper.add(userName, passWord);
                        Intent intent2 = new Intent(this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,  "密码至少6位，最多10位", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this,  "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,  "您有信息未填写完整，请重新输入", Toast.LENGTH_SHORT).show();
            }
        }
    }




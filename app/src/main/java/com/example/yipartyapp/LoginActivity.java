package com.example.yipartyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    /**
     * 声明DBOpenHelper 对象，目的---创建数据表，并对数据表进行操作
     */
    private DBOpenHelper mDBOpenHelper;
    /**
     * 用户输入的用户名
     */
    private EditText userNameInput;
    /**
     * 用户输入的密码
     */
    private EditText passWordInput;
    /**
     * 重写 onCreate() 方法
     * 保存实例状态（Android Activity默认方法）
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();//初始化
        mDBOpenHelper = new DBOpenHelper(this);//实例化 DBOpenHelper
    }

    private void initView() {
        userNameInput=findViewById(R.id.userName);//获取用户输入的用户名
        passWordInput=findViewById(R.id.password);//获取用户输入的密码
    }

    /**
     * public公共方法，向外暴露，好让activity_login可以内联
     * 登陆事件
     * @param view
     */
    public void login(View view){
       String userName=userNameInput.getText().toString().trim();//将输入的用户名去掉左右空格后赋值给userName
       String passWord=passWordInput.getText().toString().trim();//将输入的密码去掉左右空格后赋值给passWord
        //先进行基础的内容非空判断
        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord)){
            ArrayList<User> data=mDBOpenHelper.getAllData();//将数据库的值全部取出存放到data中
            boolean match=false;//设定一个判断标记
            //遍历data
            for(int i=0;i < data.size();i++){
                User user=data.get(i);
                if(userName.equals(user.getUserName()) && passWord.equals(user.getPassWord())){
                    match=true;//验证正确，给定标记为true
                    break;
                }else {
                    match=false;
                    break;
                }
            }
            if(match){
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();//销毁此Activity
            } else {
                Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    //注册跳转
    public void regTo(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
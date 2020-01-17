package com.example.yipartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyDialog mMyDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //用户信息提醒弹窗
    @Override
    public void onClick(View view) {
        view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

    }
}
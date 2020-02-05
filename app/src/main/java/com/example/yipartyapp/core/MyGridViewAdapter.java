package com.example.yipartyapp.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.WindowSize;

public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    int num;
    int col;

    public MyGridViewAdapter(Context context, int num, int col) {
        this.context = context;
        this.num = num;
        this.col = col;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return num;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int width = WindowSize.getWidth(context);// 获取屏幕宽度
        Log.i("tag", "width" + width);
        int height = 0;
        width = width / col;// 对当前的列数进行设置imgView的宽度
        height = width;
        img.setLayoutParams(new AbsListView.LayoutParams(width, height));
        img.setImageResource(R.drawable.welcome);
        img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + position, 0).show();
            }
        });
        return img;
    }
}

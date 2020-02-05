package com.example.yipartyapp.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.yipartyapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    List<String> data;

    public MyListViewAdapter(Context context) {
        this.context = context;
        //生成9条数据
        data = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            data.add("");
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 10;// 返回10条数据
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item,
                    parent, false);
            holder.gridview = (PictureGridView) convertView
                    .findViewById(R.id.gridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int num = position % data.size() + 1;//获取当前的图片数目

        int col = 1;//默认列数
        Log.i("tag", "num" + num);
        if (num == 1) {
            holder.gridview.setNumColumns(1);

            col = 1;
        } else if (num == 2 || num == 4) {
            holder.gridview.setNumColumns(2);
            col = 2;
        } else {
            holder.gridview.setNumColumns(3);
            col = 3;
        }

        holder.gridview.setAdapter(new MyGridViewAdapter(context, num, col));

        holder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Toast.makeText(context, "" + position, 0).show();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        PictureGridView gridview;
    }

}

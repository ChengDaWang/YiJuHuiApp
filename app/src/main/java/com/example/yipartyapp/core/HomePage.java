package com.example.yipartyapp.core;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.yipartyapp.R;

public class HomePage extends Fragment {
private ListView listView;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_message_tp,container,false);
        ListView listView=(ListView)view.findViewById(R.id.listview);
        //Fragment无法直接转换Activity的获取上下文context方法，只能调用Fragment的getContent来获取
        listView.setAdapter(new MyListViewAdapter(getContext()));
        return view;

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

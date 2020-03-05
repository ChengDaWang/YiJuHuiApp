package com.example.yipartyapp.core.MinePage_headImage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.R;
import com.example.yipartyapp.bean.ID;
import com.example.yipartyapp.bean.IDMsg;

import java.util.ArrayList;
import java.util.List;

public class IdentityActivity extends AppCompatActivity{

    private ListView idList;
    private TextView textView;
    private DBOpenHelper mDBOpenHelper;
    private List<IDMsg> mList= new ArrayList<>();
    public TextView job, school, adress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        mDBOpenHelper = new DBOpenHelper(this);
        ArrayList<ID> data=mDBOpenHelper.getID();
         for(ID mid:data){
            IDMsg mdata =new IDMsg();
            mdata.setSchool(mid.getSchool());
            mdata.setAdress(mid.getAdress());
            mdata.setBeginData(mid.getBeginData());
            mdata.setEndData(mid.getEndData());
            mdata.setJob(mid.getJob());
            mList.add(mdata);
        }

        idList = findViewById(R.id.identity_message);
        //设置适配器
        idList.setAdapter(new BaseAdapter(){
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if(convertView == null ){
                    LayoutInflater inflater = IdentityActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.list_identity,null);
                }else {
                    view = convertView;
                }
                IDMsg idMsg=mList.get(position);
                TextView job=view.findViewById(R.id.textView48);
                TextView school=view.findViewById(R.id.textView49);
                TextView adress=view.findViewById(R.id.textView50);

                job.setText(idMsg.getJob());
                school.setText(idMsg.getSchool()+" | "+idMsg.getBeginData()+" — "+idMsg.getEndData());
                adress.setText(idMsg.getAdress());
                return view;
            }
        });
        textView = findViewById(R.id.textView47);
    }

    public void onClickToGo(View v) {
        Intent intent = new Intent(this, IdCompileActivity.class);
        startActivity(intent);
        finish();
    }
}

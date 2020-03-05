package com.example.yipartyapp.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.R;
import com.example.yipartyapp.bean.Info;
import com.example.yipartyapp.bean.headImage;
import com.example.yipartyapp.core.MinePage_headImage.AboutVersionActivity;
import com.example.yipartyapp.core.MinePage_headImage.IdentityActivity;
import com.example.yipartyapp.core.MinePage_headImage.MineInfoActivity;
import com.example.yipartyapp.core.MinePage_headImage.UpPasswordActivity;
import com.example.yipartyapp.core.PopupWindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

//import static com.example.yipartyapp.core.MinePage_headImage.MineInfoActivity.loadCircleImage;

public class MinePage extends Fragment implements CommonPopupWindow.ViewInterface,
        EasyPermissions.PermissionCallbacks{
    private DBOpenHelper mDBOpenHelper;
    private TextView textView;//姓名
    private TextView textView1;//学校
    private ImageView toGo1,headImage1,toGo2,toGo3,toGo4;
    private CommonPopupWindow commonPopupWindow;
    private ImageView mIcon;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_mine_page,container,false);
        mDBOpenHelper = new DBOpenHelper(getContext());
        textView=(TextView)view.findViewById(R.id.textView10);
        ArrayList<Info> data=mDBOpenHelper.getInfo();
        //用户名加载
        String name=data.get(0).getName();
        textView.setText(name);
        //学校加载
        String school=data.get(0).getSchool();
        textView1=(TextView)view.findViewById(R.id.textView13);
        textView1.setText(school);
        //头像加载
        headImage1=(ImageView)view.findViewById(R.id.icon);
        ArrayList<headImage> image=mDBOpenHelper.getHeadImage();
        //获取base64码
        if(image.size() != 0) {
            if (image.get(0).getHeadImage() != null) {
                String base64Data = image.get(0).getHeadImage();
                //将base64转换成bitmap
                Bitmap bitmap = base64ToBitmap(base64Data);
                loadCircleImage(getContext(), bitmap, headImage1);
            }
        }
        toGo1=(ImageView)view.findViewById(R.id.imageView4);
        toGo1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MineInfoActivity.class);
                startActivity(intent);
            }
        });
        //修改密码
        toGo2=view.findViewById(R.id.imageView6);
        toGo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), UpPasswordActivity.class);
                startActivity(intent);
            }
        });
        //版本查看
        toGo3=view.findViewById(R.id.imageView7);
        toGo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AboutVersionActivity.class);
                startActivity(intent);
            }
        });
        //身份管理
        toGo4=view.findViewById(R.id.imageView8);
        toGo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), IdentityActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void getChildView(View view, int layoutResId) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * base64转码bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, Bitmap bitmap, ImageView imageView) {
        // RequestOptions  扩展glide  自定义加载方式
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(bitmap).apply(options).into(imageView);
    }
}

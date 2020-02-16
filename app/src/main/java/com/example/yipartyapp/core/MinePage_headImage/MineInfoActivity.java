package com.example.yipartyapp.core.MinePage_headImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.PhotoUtils;
import com.example.yipartyapp.Utils.ToastUtils;
import com.example.yipartyapp.core.PopupWindow.CommonPopupWindow;
import com.example.yipartyapp.core.PopupWindow.CommonUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MineInfoActivity extends AppCompatActivity implements CommonPopupWindow.ViewInterface{

    private DBOpenHelper mDBOpenHelper;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private CommonPopupWindow commonPopupWindow;
    private ImageView imageView3;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    //Uri代表要操作的数据，Android上可用的每种资源 - 图像、视频片段等都可以用Uri来表示。换句话说：android系统中任何可用的资源（图像、视频、文件）
    // 都可以用uri表示。
    //uri讲解：
    //1.uri属性有以下4部分组成：android:scheme、android:host、android:port、android:path
    //其中host和port2个统称为authority。
    //2.要使authority（host和port）有意义，必须指定scheme；要使path有意义，必须使scheme和authority（host和port）有意义
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath()+ "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private LinearLayout linearLayout1;//更换头像
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);

        mDBOpenHelper = new DBOpenHelper(this);
        /*Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
        loadCircleImage(this,bitmap,imageView3);*/
        linearLayout1=(LinearLayout)findViewById(R.id.li1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll(v);
            }
        });
    }

    //全屏弹出
    public void showAll(View view) {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        commonPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_up:
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = (Button) view.findViewById(R.id.btn_select_photo);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

                /**
                 * 相机
                 */
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        autoObtainCameraPermission();
                        commonPopupWindow.dismiss();
                    }
                });
                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autoObtainStoragePermission();
                        commonPopupWindow.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
        }
    }
    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }
    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {


                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    imageUri = FileProvider.getUriForFile(MineInfoActivity.this, "com.example.yipartyapp", fileUri);

                }else {
                    imageUri = Uri.fromFile(fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       switch (requestCode) {
           //调用系统相机申请拍照权限回调
           case CAMERA_PERMISSIONS_REQUEST_CODE: {
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   if (hasSdcard()) {
                       imageUri = Uri.fromFile(fileUri);
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                           imageUri = FileProvider.getUriForFile(MineInfoActivity.this, "com.example.yipartyapp", fileUri);//通过FileProvider创建一个content类型的Uri
                       PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                   } else {
                       ToastUtils.showShort(this, "设备没有SD卡！");
                   }
               } else {

                   ToastUtils.showShort(this, "请允许打开相机！！");
               }
               break;


           }
           //调用系统相册申请Sdcard权限回调
           case STORAGE_PERMISSIONS_REQUEST_CODE:
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
               } else {

                   ToastUtils.showShort(this, "请允许打操作SDCard！！");
               }
               break;
           default:
       }
   }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.example.yipartyapp", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    bitmapToBase64(bitmap);
                    loadCircleImage(this,bitmap,imageView3);
                    /*if (bitmap != null) {
                        showImages(bitmap);
                    }*/
                    break;
                default:
            }
        }
    }
    /**
     * 将图片转码Base64
     */
    private  String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDBOpenHelper.addHeadImage(result);
        return result;
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

package com.example.yipartyapp.core.recommendPage_ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yipartyapp.R;

import java.util.ArrayList;
import java.util.List;

public class advertising extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    private LinearLayout mPointContainer;
    private RelativeLayout rl_root;
    private View mSeletedPoint;

    private int             mPointSpace;
    private List<ImageView> mImgDatas;
    //在studio中引用图片是通过 R.mipmap.guide_1
    //在eclipse中是通过R.drawable.guide_1
    private int[]           imgRes  = new int[] {
            R.mipmap.login,
            R.mipmap.error_picture,
            R.mipmap.lvyou
    };
    private int screenHeight;
    private int screenWidth;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advertising_url, container, false);
        screenHeight=getScreenHeight(getContext());
        screenWidth=getScreenWidth(getContext());
        mPager = (ViewPager)view.findViewById(R.id.guide_pager);
        mPointContainer = (LinearLayout)view.findViewById(R.id.gudie_point_container);
        mSeletedPoint = view.findViewById(R.id.guide_point_selected);
        rl_root=(RelativeLayout)view.findViewById(R.id.rl_root);
        rl_root.setLayoutParams(new FrameLayout.LayoutParams(screenWidth,screenHeight));
        // 添加全局布局的监听
        mSeletedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // 获得两个点间的距离(实时监听全局，只要出现两个之后就获取距离并移除监听)
                mPointSpace = mPointContainer.getChildAt(1).getLeft() - mPointContainer.getChildAt(0).getLeft();
                mSeletedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        initData();
        return view;
    }

    private void initData() {
        // 对数据进行初始化
        mImgDatas = new ArrayList<ImageView>();
        for (int i = 0; i < imgRes.length; i++)
        {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(imgRes[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            // add
            mImgDatas.add(iv);

            // 动态的添加点
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.point_normal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(getContext(),10), dip2px(getContext(),10));
            if (i != 0)
            {
                params.leftMargin = 10;
            }
            mPointContainer.addView(view, params);
        }
        // 给viewpager添加数据
        mPager.setAdapter(new advertising.GuidePagerAdapter());// --->List

        // 设置viewpager的监听
        mPager.setOnPageChangeListener(this);
    }

    class GuidePagerAdapter extends PagerAdapter {
        @Override
        public int getCount()
        {
            if (mImgDatas != null) { return mImgDatas.size(); }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            // 获得imageView
            ImageView iv = mImgDatas.get(position);
            // 用pager添加
            // mPager.addView(iv);
            container.addView(iv);

            return iv;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            //加载当前页面的左右两侧数据，其他的都会实时消除
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 滚动时
        // @positionOffset :
        // @positionOffsetPixels: 滚动的像素

        // 1. 计算需要移动距离
        // 通过两个点间的距离计算 和 positionOffset，移动的距离
        //positionOffset的值是[0-1]移动的百分比
        int left = (int) (mPointSpace * positionOffset + 0.5f);

        // 2. 改变选中点的marginleft
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mSeletedPoint.getLayoutParams();
        params.leftMargin = left + mPointSpace * position;

        mSeletedPoint.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 把(Xml中)dp转换为像素px展现
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }
    /**
     * 得到设备的(Xml中)dp
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}

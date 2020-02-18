package com.example.yipartyapp.core.recommendPage_ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.CustomDecoration;
import com.example.yipartyapp.Utils.GetJsonDataUtil;
import com.example.yipartyapp.Utils.ToastUtils;
import com.example.yipartyapp.bean.MerchantBaseBean;
import com.example.yipartyapp.bean.MerchantBean;
import com.example.yipartyapp.core.adapters.RecyclerViewAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品展示浏览
 */
public class MerchantFragment extends Fragment {

    private ImageView mLoadingView;
    private RefreshLayout refreshLayout;
    private boolean refreshType;//数据刷新标记
    private int page;
    private RecyclerView rvMovieList;
    private List<MerchantBean> mList = new ArrayList<>();
    private int oldListSize;
    private int newListSize;
    private int addListSize;
    private RecyclerViewAdapter adapter;
    private LinearLayout loading_view_ll;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_merchant,container,false);

        //动画加载初始化
        mLoadingView=(ImageView)view.findViewById(R.id.loading_view);
        refreshLayout=(RefreshLayout)view.findViewById(R.id.refreshLayout);
        rvMovieList=(RecyclerView)view.findViewById(R.id.rvMovieList);
        loading_view_ll=(LinearLayout)view.findViewById(R.id.loading_view_ll);
        initView();
        initData();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 数据加载动画（这里用的是获取本地数据来模拟真实场景从网上服务器获取json数据的过程时动画的加载）
     */
    protected void initView() {
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    protected void initData() {

        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = true;
                        page = 1;
                        merchantJson();
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = false;
                        if (page > 2) {
                            ToastUtils.showToast("暂无更多的数据啦");
                            // 将不会再次触发加载更多事件
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            return;
                        }
                        merchantJson();
                        refreshLayout.setEnableLoadMore(true);
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();
    }

    private void merchantJson() {
        try {
            // 从assets目录中获取json数据，在真实的项目开发中需要通过网络请求从服务器json数据
            String jsonData = new GetJsonDataUtil().getJson(getContext(),"merchant1.json");
            if (refreshType && mList != null) {
                mList.clear();
                oldListSize = 0;
            } else {
                oldListSize = mList.size();
            }
            // 使用Google的Gson开始解析json数据
            Gson gson = new Gson();
            MerchantBaseBean merchantBaseBean=gson.fromJson(jsonData,MerchantBaseBean.class);
            List<MerchantBean> merchantBeanList=merchantBaseBean.getData();
            for(MerchantBean merchantBean : merchantBeanList){
                MerchantBean data=new MerchantBean();
                data.setMerchantName(merchantBean.getMerchantName());//商户名称
                data.setAdresss(merchantBean.getAdresss());//商户地址
                data.setMoney(merchantBean.getMoney());//商品金额
                mList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        newListSize = mList.size();
        addListSize = newListSize - oldListSize;

        if (refreshType) {
            // 设置RecyclerView样式为竖直线性布局
            rvMovieList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new RecyclerViewAdapter(getContext(), mList);
            /*if (viewType.equals("NoDividingLine")) {
                mTitleBar.setTitle("线性布局样式");
            } else {*/

            // 设置分割线
            rvMovieList.addItemDecoration(new CustomDecoration(
                    getContext(), LinearLayoutManager.VERTICAL, R.drawable.divider_mileage, 15));

            rvMovieList.setAdapter(adapter);
        } else {
            adapter.notifyItemRangeInserted(mList.size() - addListSize, mList.size());
            adapter.notifyItemRangeChanged(mList.size() - addListSize, mList.size());
        }
        page++;

        rvMovieList.setVisibility(View.VISIBLE);
        loading_view_ll.setVisibility(View.GONE);

        // item条目的点击事件回调
        adapter.setItemClikListener(new RecyclerViewAdapter.OnItemClikListener() {

            // 短按点击事件回调
            @Override
            public void onItemClik(View view, int position) {
                String videoTitle = mList.get(position).getMerchantName();
                //ToastUtils.showToast(videoTitle);
                Intent detlis=new Intent(getContext(),MerchantDetlisActivity.class);
                detlis.putExtra("merchantName",mList.get(position).getMerchantName());
                detlis.putExtra("merchantAdress",mList.get(position).getAdresss());
                detlis.putExtra("merchantMoney",mList.get(position).getMoney());
                startActivity(detlis);
            }

            // 长按点击事件回调
            @Override
            public void onItemLongClik(View view, int position) {

            }
        });

    }
}

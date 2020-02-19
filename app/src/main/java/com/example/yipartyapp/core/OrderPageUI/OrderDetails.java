package com.example.yipartyapp.core.OrderPageUI;


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

import com.example.yipartyapp.DBOpenHelper;
import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.CustomDecoration;
import com.example.yipartyapp.Utils.ToastUtils;
import com.example.yipartyapp.bean.OrderMsg;
import com.example.yipartyapp.bean.order;
import com.example.yipartyapp.core.adapters.OrderRecycleAdapter;
import com.example.yipartyapp.core.adapters.RecyclerViewAdapter;
import com.example.yipartyapp.core.recommendPage_ui.MerchantDetlisActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 */
public class OrderDetails extends Fragment {

    private ImageView mLoadingView;
    private RefreshLayout refreshLayout;
    private boolean refreshType;//数据刷新标记
    private int page;
    private RecyclerView ryOrder;
    private int oldListSize;
    private int newListSize;
    private int addListSize;
    private OrderRecycleAdapter adapter;
    private LinearLayout loading_view_ll;
    private List<OrderMsg> mList= new ArrayList<>();
    private DBOpenHelper mDBOpenHelper;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_details, container, false);
        mDBOpenHelper=new DBOpenHelper(getContext());
        //动画加载初始化
        /**
         * 这里的动画加载原理与推荐一样的就不再做注释了，唯一的不同就是RecyclerView的数据获取不再是
         * Json数据格式，这里是从数据库里面获取数据
         */
        mLoadingView=(ImageView)view.findViewById(R.id.loading_view);
        refreshLayout=(RefreshLayout)view.findViewById(R.id.refreshLayout);
        ryOrder=(RecyclerView)view.findViewById(R.id.rvMovieList);
        loading_view_ll=(LinearLayout)view.findViewById(R.id.loading_view_ll);
        initView();
        initData();
        return view;
    }

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
                        orderData();
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
                        orderData();
                        refreshLayout.setEnableLoadMore(true);
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();
    }

    private void orderData(){
        try{
            List<order> msgList=mDBOpenHelper.getOrder();
            //List<OrderMsg> msgList1=msgList.get(0);
            for(order  morder:msgList){
                OrderMsg data=new OrderMsg();
                data.setName(morder.getName());
                data.setMoney(morder.getMoney());
                mList.add(data);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        newListSize = mList.size();
        addListSize = newListSize - oldListSize;

        if (refreshType) {
            // 设置RecyclerView样式为竖直线性布局
            ryOrder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new OrderRecycleAdapter(getContext(), mList);
            /*if (viewType.equals("NoDividingLine")) {
                mTitleBar.setTitle("线性布局样式");
            } else {*/

            // 设置分割线
            ryOrder.addItemDecoration(new CustomDecoration(
                    getContext(), LinearLayoutManager.VERTICAL, R.drawable.divider_mileage, 15));

            ryOrder.setAdapter(adapter);
        } else {
            adapter.notifyItemRangeInserted(mList.size() - addListSize, mList.size());
            adapter.notifyItemRangeChanged(mList.size() - addListSize, mList.size());
        }
        page++;

        ryOrder.setVisibility(View.VISIBLE);
        loading_view_ll.setVisibility(View.GONE);

        // item条目的点击事件回调
        adapter.setItemClikListener(new OrderRecycleAdapter.OnItemClikListener() {

            // 短按点击事件回调
            @Override
            public void onItemClik(View view, int position) {

            }

            // 长按点击事件回调
            @Override
            public void onItemLongClik(View view, int position) {

            }
        });

    }
}


package com.example.yipartyapp.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.yipartyapp.R;
import com.example.yipartyapp.Utils.Utils;
import com.example.yipartyapp.bean.FriendCircleBean;
import com.example.yipartyapp.core.adapters.FriendCircleAdapter;
import com.example.yipartyapp.interfaces.OnPraiseOrCommentClickListener;

import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class dongtaiActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        OnPraiseOrCommentClickListener, ImageWatcher.OnPictureLongPressListener, ImageWatcher.Loader {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Disposable mDisposable;
    private FriendCircleAdapter mFriendCircleAdapter;
    private ImageWatcher mImageWatcher;

    /**
     *  初始化View,各组件
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_dongtai,container,false);
        mSwipeRefreshLayout=view.findViewById(R.id.swpie_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(dongtaiActivity.this).resumeRequests();
                } else {
                    Glide.with(dongtaiActivity.this).pauseRequests();
                }
            }

            /**
             *绘制RecyclerView视图
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mImageWatcher = view.findViewById(R.id.image_watcher);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new FriendsCircleAdapterDivideLine());
        mFriendCircleAdapter = new FriendCircleAdapter(getContext(), recyclerView, mImageWatcher);
        recyclerView.setAdapter(mFriendCircleAdapter);
        mImageWatcher.setTranslucentStatus(Utils.calcStatusBarHeight(getContext()));
        mImageWatcher.setErrorImageRes(R.mipmap.error_picture);
        mImageWatcher.setOnPictureLongPressListener(this);
        mImageWatcher.setLoader(this);
        Utils.showSwipeRefreshLayout(mSwipeRefreshLayout, this::asyncMakeData);
        return view;
    }

    private void asyncMakeData() {
        mDisposable = Single.create((SingleOnSubscribe<List<FriendCircleBean>>) emitter ->
                emitter.onSuccess(DataCenter.makeFriendCircleBeans(getContext())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((friendCircleBeans, throwable) -> {
                    Utils.hideSwipeRefreshLayout(mSwipeRefreshLayout);
                    if (friendCircleBeans != null && throwable == null) {
                        mFriendCircleAdapter.setFriendCircleBeans(friendCircleBeans);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onRefresh() {
        asyncMakeData();
    }

    @Override
    public void load(Context context, String url, ImageWatcher.LoadCallback lc) {
        Glide.with(context).asBitmap().load(url).into(new GlideSimpleTarget(lc));
    }

    @Override
    public void onPictureLongPress(ImageView v, String url, int pos) {

    }

    @Override
    public void onPraiseClick(int position) {
        Toast.makeText(getContext(), "You Click Praise!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentClick(int position) {

    }

    public void onBackPressed(){
       /* if (!mImageWatcher.handleBackPressed()){
            super.onBackPressed();
        }*/
    }
  /*  @Override
    public void onBackPressed() {
        *//*if (!mImageWatcher.handleBackPressed()) {
            super.onBackPressed();
        }*//*
    }*/

}

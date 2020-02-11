package com.example.yipartyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.yipartyapp.core.MinePage;
import com.example.yipartyapp.core.RecommendPage;
import com.example.yipartyapp.core.HomePage;
import com.example.yipartyapp.core.OrderPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {



    private BottomNavigationView bottomNavigationView;
    private RecommendPage recommendPage;
    private HomePage homePage;
    private OrderPage orderPage;
    private MinePage minePage;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    //初始化页面
    private void initFragment()
    {

        recommendPage = new RecommendPage();
        homePage = new HomePage();
        orderPage = new OrderPage();
        minePage = new MinePage();
        fragments = new Fragment[]{homePage,recommendPage,orderPage,minePage};
        lastfragment=0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,homePage).show(homePage).commit();

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }
    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.id1://首页
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;

                    }

                    return true;
                }
                case R.id.id2://推荐
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;

                    }

                    return true;
                }
                case R.id.id3://订单
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;

                    }

                    return true;
                }
                case R.id.id4://我的
                {
                    if(lastfragment != 3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;
                    }
                }

            }

            return false;
        }
    };
    //切换Fragment
    private void switchFragment(int lastfragment,int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.mainview, fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}
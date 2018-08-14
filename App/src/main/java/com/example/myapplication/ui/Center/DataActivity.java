package com.example.myapplication.ui.Center;

import android.view.View;
import android.widget.TableRow;

import com.example.library.Animation.PageNumberPoint;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AppsViewPagerAdapter;
import com.example.myapplication.adapter.ControlViewPager;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资料页面
 * Author:许格(软件部) 2018-07-24
 */

public class DataActivity extends BaseActivity {

    @BindView(R.id.pager) ControlViewPager mPager;
    @BindView(R.id.indicator_layout) PageNumberPoint mIndicatorLayout;
    @BindView(R.id.change_pwd) TableRow pwd;
    @BindView(R.id.change_back) TableRow back;

    private List<BaseFragment> baseFragments;
    private AppsViewPagerAdapter appsViewPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_data;
    }

    @Override
    public void initView() {
        baseFragments=createListFragments();
        appsViewPagerAdapter = new AppsViewPagerAdapter(getSupportFragmentManager(), baseFragments);
        mPager.setAdapter(appsViewPagerAdapter);
        mIndicatorLayout.addViewPager(mPager);

    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.change_pwd, R.id.change_back, R.id.iv_data_back})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.change_pwd:
                mPager.setCurrentItem(1);
                back.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.GONE);
                break;
            case R.id.change_back:
                mPager.setCurrentItem(0);
                back.setVisibility(View.GONE);
                pwd.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_data_back:
                DataActivity.this.finish();
                break;
        }
    }

    private  List<BaseFragment> createListFragments() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new DataFragment());
        list.add(new PwdFragment());
        return list;
    }
}

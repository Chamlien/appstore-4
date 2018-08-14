package com.example.myapplication.ui.Center;

import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

import butterknife.OnClick;

/**
 * 推广页面
 * Author:许格(软件部) 2018-07-26
 */

public class SpreadActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_spread;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.spread_close)
    void OnClick(View view){
        switch (view.getId()){
            case R.id.spread_close:
                SpreadActivity.this.finish();
                break;
        }
    }
}

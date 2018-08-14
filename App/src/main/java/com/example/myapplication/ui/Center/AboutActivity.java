package com.example.myapplication.ui.Center;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.utils.systemUtils.AppUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于页面
 * Author:许格(软件部) 2018-07-26
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_version) TextView version;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        String appVersion = AppUtil.getVersion(this);
        version.setText("康复应用商店 " + appVersion);
    }

    @OnClick(R.id.about_close)
    void OnClick(View view){
        switch (view.getId()){
            case R.id.about_close:
                AboutActivity.this.finish();
                break;
        }
    }
}

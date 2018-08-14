package com.example.myapplication.ui.Center;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.utils.viewUtils.T;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.switch1)Switch aSwitch;
    @BindView(R.id.switch2)Switch aSwitch2;
    @BindView(R.id.switch3)Switch aSwitch3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        aSwitch.setChecked(true);
        aSwitch2.setChecked(false);
        aSwitch3.setChecked(true);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aSwitch.setSwitchTextAppearance(SettingActivity.this,R.style.switch_true);
                }else {
                    aSwitch.setSwitchTextAppearance(SettingActivity.this,R.style.switch_false);
                }
            }
        });
    }

    @OnClick({R.id.setting_close,R.id.history_version,R.id.stop_service})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.setting_close:
                SettingActivity.this.finish();
                break;
            case R.id.history_version:
                T.show(this,"暂未开发",0);
                break;
            case R.id.stop_service:

                break;
        }
    }

}

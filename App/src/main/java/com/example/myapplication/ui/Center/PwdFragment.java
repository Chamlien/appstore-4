package com.example.myapplication.ui.Center;


import android.animation.ValueAnimator;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.library.ProgressButton.circular.CircularProgressButton;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PwdFragment extends BaseFragment {

    @BindView(R.id.bt_pwd_up)
    CircularProgressButton button;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pwd;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.bt_pwd_up})
    void onClick(View v){
        switch (v.getId()){
            case R.id.bt_pwd_up:
                button.setProgress(50);
                SuccessProgress(button);
                break;
        }
    }

    private void SuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(2000);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }
}

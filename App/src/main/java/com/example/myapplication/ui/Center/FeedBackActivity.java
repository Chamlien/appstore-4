package com.example.myapplication.ui.Center;

import android.animation.ValueAnimator;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.example.library.ProgressButton.circular.CircularProgressButton;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.utils.textUtils.PhoneFormatUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务反馈页面
 * Author:许格(软件部) 2018-07-30
 */

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.et_feed_theme) EditText theme;
    @BindView(R.id.et_feed_content) EditText content;
    @BindView(R.id.et_feed_phone) EditText phone;
    @BindView(R.id.et_feed_address) EditText address;
    @BindView(R.id.bt_feed_up)CircularProgressButton button;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        setEditTextInhibitInputSpeChat(theme);
        setEditTextInhibitInputSpeChat(content);
        setEditTextInhibitInputSpeChat(address);
        phone.addTextChangedListener(new PhoneFormatUtils(phone));
    }

    @OnClick({R.id.bt_feed_up,R.id.iv_feed_back})
    void onClick(View v){
        switch (v.getId()){
            case R.id.iv_feed_back:
                FeedBackActivity.this.finish();
                break;
            case R.id.bt_feed_up:
                if (theme.getText().length()==0 || content.getText().length()==0 ||
                    phone.getText().length()==0 || address.getText().length()==0){
                    ErrorProgress(button);
                    T.show(this,"输入不能为空",1);
                }else {
                    SuccessProgress(button);
                }
                break;
        }
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 成功时动画
     * @param button
     */
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

    /**
     * 失败动画
     * @param button
     */
    private void ErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(-1, 0);
        widthAnimation.setDuration(500);
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

package com.example.myapplication.ui.Activity.other;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.ui.Apps.Fragment.AppFragment;
import com.example.myapplication.utils.helper.JellyInterpolator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/01 08:53
 * 4 * Desc:登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username) EditText username;
    @BindView(R.id.et_password) EditText password;
    @BindView(R.id.bt_go) Button login;
    @BindView(R.id.layout_edit) LinearLayout edit;
    @BindView(R.id.layout_progress) LinearLayout progress;
    @BindView(R.id.layout_user) LinearLayout view_user;
    @BindView(R.id.layout_pwd) LinearLayout view_pwd;
    @BindView(R.id.login_message) TextView logMeg;

    private int mWidth,mHeight;
    private Handler handler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        username.setText("admin");
        username.setSelection(username.getText().length());
        fileOutput();

        /**实时动态更新UI界面**/
        handler=new Handler(){
          public void handleMessage(Message message){
              switch (message.what){
                  case 0x111:
                      fileInput();
                      logMeg.setVisibility(View.INVISIBLE);
                      login.setText("登录成功");
                      handler.sendEmptyMessageDelayed(0x555,1000);
                      break;
                  case 0x222:
                      logMeg.setVisibility(View.VISIBLE);
                      logMeg.setTextColor(getResources().getColor(R.color.cpb_red));
                      logMeg.setText("用户名或密码错误");
                      handler.sendEmptyMessageDelayed(0x333,1000);
                      break;
                  case 0x333:
                      recovery();
                      break;
                  case 0x555:
                      LoginActivity.this.finish();
                      break;
              }
              super.handleMessage(message);
          }
        };
    }

    /**保存用户密码**/
    private void fileInput() {
        File file = new File(getCacheDir(),"Login.txt");
        try {
            //写流
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((username.getText().toString() + "##" + password.getText().toString()).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("账号密码保存失败", e.toString());
        }
    }

    /**读取用户已保存的密码**/
    private void fileOutput(){
        File file = new File(getCacheDir(),"Login.txt");
        if(file.exists() && file.length() > 0){
            try {
                //读流
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String QQLogin = br.readLine();
                //帐号密码用##分隔，所以用##切割
                String user_name = QQLogin.split("##")[0];
                String pwd = QQLogin.split("##")[1];
                //并将缓存的帐号密码显现
                username.setText(user_name);
                password.setText(pwd);
                fis.close();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.bt_go,R.id.login_close})
    void onClick(View v){
        switch (v.getId()){
            case R.id.bt_go:
                mWidth=login.getMeasuredWidth();
                mHeight=login.getMeasuredHeight();
                view_user.setVisibility(View.INVISIBLE);
                view_pwd.setVisibility(View.INVISIBLE);
                inputAnimator(edit,mWidth,mHeight);
                break;
            case R.id.login_close:
                LoginActivity.this.finish();
                break;
        }
    }

    /**提示界面刷新**/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message m = handler.obtainMessage();
            Message message = AppFragment.handler.obtainMessage();
            String name = username.getText().toString();
            String pwd = password.getText().toString();
            if (name.equals("admin")&&pwd.equals("admin")){
                m.what = 0x111;
                message.what = 2;
            }else {
                m.what = 0x222;
            }
            handler.sendMessage(m);
            AppFragment.handler.sendMessage(message);
        }
    };
    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(edit,
                "scaleX", 1f, 0.5f);
        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                edit.setVisibility(View.INVISIBLE);
                new Thread(runnable).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }

    /**
     * 出现进度动画
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    /**
     * 恢复初始状态
     */
    private void recovery(){
        progress.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        view_user.setVisibility(View.VISIBLE);
        view_pwd.setVisibility(View.VISIBLE);
        logMeg.setVisibility(View.INVISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) edit.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        edit.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(edit, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
}

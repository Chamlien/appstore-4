package com.example.myapplication.ui.Center;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.library.Animation.WaveFunctionView;
import com.example.myapplication.R;
import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;
import com.example.myapplication.base.BaseFragment;
import com.example.myapplication.ui.Activity.other.LoginActivity;
import com.example.myapplication.ui.Update.MVP.UpdatePresenter;
import com.example.myapplication.ui.Update.MVP.UpdateView;
import com.example.myapplication.utils.systemUtils.AppUtil;
import com.example.myapplication.utils.viewUtils.T;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/7/01 08:53
 * 4 * Desc:个人中心页面
 */

public class CenterFragment extends BaseFragment implements UpdateView{

    @BindView(R.id.BezierView)
    WaveFunctionView wave;
    @BindView(R.id.version_tips)
    TextView tips;

    UpdatePresenter presenter;
    boolean isUpdate = false;
    String newVersion;
    String newSize;
    String newFunction;
    String urls;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_center;
    }

    @Override
    protected void initView() {
        presenter = new UpdatePresenter(this,getActivity());
        wave.postDelayed(new Runnable() {
            @Override
            public void run() {
                wave.ChangeWaveLevel(1);
            }
        }, 1000);
        String packageName = AppUtil.getPackage(getActivity());
        String version = AppUtil.getVersion(getActivity());
        presenter.loadVersion(packageName,version);
    }

    @OnClick({R.id.center_message,R.id.max_name,R.id.center_cup,R.id.center_buy,
        R.id.center_put,R.id.center_about,R.id.center_versionUpdate,R.id.center_callus,
        R.id.center_feedback,R.id.center_assess, R.id.center_setting})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.max_name: //登录
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;
            case R.id.center_message: //资料
                startActivity(new Intent(getActivity(),DataActivity.class));
                break;
            case R.id.center_cup: //商城
                startActivity(new Intent(getActivity(),MallActivity.class));
                break;
            case R.id.center_buy: //购买记录
                startActivity(new Intent(getActivity(),PurchaseActivity.class));
                break;
            case R.id.center_put: //推广
                startActivity(new Intent(getActivity(),SpreadActivity.class));
                break;
            case R.id.center_assess: //评价
                T.show(getActivity(),"该功能暂未开发",1);
                break;
            case R.id.center_feedback://反馈
                startActivity(new Intent(getActivity(),FeedBackActivity.class));
                break;
            case R.id.center_versionUpdate: // 版本更新
                intentMessage();
                break;
            case R.id.center_callus://联系我们
                showDialog();
                break;
            case R.id.center_about://关于
                startActivity(new Intent(getActivity(),AboutActivity.class));
                break;
            case R.id.center_setting://设置
                startActivity(new Intent(getActivity(),SettingActivity.class));
                break;
        }
    }

    /**
     * 检查版本
     */
    private void intentMessage() {
        if (isUpdate==true) {
            Intent intent = new Intent();
            intent.putExtra("version",newVersion);
            intent.putExtra("function",newFunction);
            intent.putExtra("size",newSize);
            intent.putExtra("url",urls);
            intent.setClass(getActivity(),VersionUpActivity.class);
            startActivity(intent);
        }else {
            T.show(getActivity(), "已经是最新版本", 1);
        }
    }

    /**
     * 弹出框提示
     */
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("联系我们").setIcon(R.drawable.ic_phone_24dp)
                .setMessage("+ 0574 87432561 ")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(700,200);
    }

    /**
     * 查看当前版本是否需要更新
     * @param versionCodes
     */
    @Override
    public void returnVersion(VersionCode versionCodes) {
        if (versionCodes.getResult()==1){//返回值为1需要更新
            isUpdate=true;
            tips.setVisibility(View.VISIBLE);
            newVersion = versionCodes.getVersion();
            newSize = versionCodes.getSize();
            newFunction = versionCodes.getFunction();
            urls = versionCodes.getUrl();
        }else {
            isUpdate=false;
            tips.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnAppMessage(List<Apps.message> messages) {

    }

}

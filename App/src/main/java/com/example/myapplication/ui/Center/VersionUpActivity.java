package com.example.myapplication.ui.Center;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.service.APIService;
import com.example.myapplication.utils.systemUtils.ApkUtils;
import com.example.myapplication.utils.systemUtils.AppUtil;
import com.example.myapplication.widget.circleprogress.HorizontalProgressBarWithNumber;
import com.example.okgo.OkGo;
import com.example.okgo.model.Progress;
import com.example.okgo.request.GetRequest;
import com.example.service.OkDownload;
import com.example.service.download.DownloadListener;
import com.example.service.download.DownloadTask;
import com.example.service.download.LogDownloadListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本更新页面
 * Author:许格(软件部) 2018-07-26
 */

public class VersionUpActivity extends BaseActivity {

    @BindView(R.id.tv_update_newVersion) TextView version;
    @BindView(R.id.tv_update_newFunction) TextView function;
    @BindView(R.id.tv_update_newSize) TextView size;
    @BindView(R.id.tv_new_version) TextView version_new;
    @BindView(R.id.tv_old_version) TextView version_old;
    @BindView(R.id.tv_updateUp_back) TextView noUpdate;
    @BindView(R.id.bt_update_up) Button updateUp;

    private String newVersion;
    private String newFunction;
    private String newSize;
    private String url;
    private String oldVersion;
    private DownloadTask task;


    TextView speeds;
    TextView fileSize;
    HorizontalProgressBarWithNumber progressBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_up;
    }

    @Override
    public void initPresenter() {
        String PATH = Environment.getExternalStorageDirectory().getPath() + "/ApkDownload/";
        OkDownload.getInstance().setFolder(PATH);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        newVersion = intent.getStringExtra("version");
        newSize = intent.getStringExtra("size");
        newFunction = intent.getStringExtra("function");
        url = APIService.APP_UPDATE_URL+intent.getStringExtra("url");
        oldVersion = AppUtil.getVersion(this);

        version.setText("最新版本：v"+newVersion);
        size.setText("新版本大小："+newSize+"MB");
        function.setText("·"+newFunction);
        version_new.setText("v"+newVersion);
        version_old.setText("v"+oldVersion);
    }


    @OnClick({R.id.tv_updateUp_back,R.id.bt_update_up,R.id.updateUp_close})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_updateUp_back:
                VersionUpActivity.this.finish();
                break;
            case R.id.updateUp_close:
                VersionUpActivity.this.finish();
                break;
            case R.id.bt_update_up:
                showUpdate();
                break;
        }
    }

    /**
     * 开启下载
     */
    private void showUpdate() {
        if (task==null) {
            GetRequest<File> request = OkGo.<File>get(url).headers("aaa", "111")
                    .params("bbb", "222");
            task = OkDownload.request(url, request).save()
                    .register(new DesListener("DesListener"))
                    .register(new LogDownloadListener());
        }
        switch (task.progress.status) {
            case Progress.PAUSE:
            case Progress.NONE:
            case Progress.ERROR:
                task.start();
                break;
            case Progress.LOADING:
                task.pause();
                break;
            case Progress.FINISH:
                task.remove();
                break;
        }
        /** 添加一个弹出框显示下载进度 **/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater =  LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_is_update,null);
        speeds = (TextView) view.findViewById(R.id.isUpdate_speed);
        fileSize = (TextView) view.findViewById(R.id.isUpdate_fileSize);
        progressBar = (HorizontalProgressBarWithNumber) view.findViewById(R.id.isUpdate_progressBar);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setGravity(Gravity.CENTER);
        progressBar.setMax(100);
    }

    private class DesListener extends DownloadListener {

        DesListener(String tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            refreshUi(progress);
        }

        @Override
        public void onFinish(File file, Progress progress) {
        }

        @Override
        public void onRemove(Progress progress) {
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }
    }

    /**
     * 刷新下载进度
     * @param progress
     */
    private void refreshUi(Progress progress) {

       String currentSize = Formatter.formatFileSize(this, progress.currentSize);
       String totalSize = Formatter.formatFileSize(this, progress.totalSize);
       String speed = Formatter.formatFileSize(this, progress.speed);
       int fraction = (int) (progress.fraction * 100);
       fileSize.setText(currentSize + "/" + totalSize);
       speeds.setText(String.format("%s/s", speed));
       progressBar.setProgress(fraction);

        switch (progress.status) {
            case Progress.NONE: //"下载";
                break;
            case Progress.LOADING: //"暂停");
                break;
            case Progress.PAUSE: //"继续");
                break;
            case Progress.WAITING: //"等待");
                break;
            case Progress.ERROR: //"出错");
                break;
            case Progress.FINISH: //完成
                ApkUtils.install(this, new File(progress.filePath));
                task.remove();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.unRegister("DesListener");
        }
    }

    /**
     * 禁用返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }
}



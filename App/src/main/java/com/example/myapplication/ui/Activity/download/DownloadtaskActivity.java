package com.example.myapplication.ui.Activity.download;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.library.Animation.CheckBoxSample;
import com.example.myapplication.R;
import com.example.myapplication.adapter.DownloadAdapter;
import com.example.myapplication.ui.Apps.Fragment.AppFragment;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.widget.anims.LandingAnimator;
import com.example.myapplication.widget.anims.ScaleInAnimationAdapter;
import com.example.service.OkDownload;
import com.example.service.task.XExecutor;

public class DownloadtaskActivity extends AppCompatActivity implements View.OnClickListener,XExecutor.OnAllTaskEndListener{

    private TextView noTask;
    private RecyclerView mList;
    private TableRow cleanall;
    private TableRow stopall;
    private TableRow setting;
    private ImageView close;
    private DownloadAdapter mAdapter;
    private OkDownload okDownload;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadtask);
        initView();
        okDownload = OkDownload.getInstance();
        mAdapter = new DownloadAdapter(this);
        mAdapter.updateData(DownloadAdapter.TYPE_ALL);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
        mList.setItemAnimator(new LandingAnimator());
        mList.setAdapter(new ScaleInAnimationAdapter(mAdapter));
        okDownload.addOnAllTaskEndListener(this);
        if (mAdapter.getItemCount()>0){
            noTask.setVisibility(View.GONE);
        }else {
            noTask.setVisibility(View.VISIBLE);
        }

        handler = new Handler(){
          private void handlerMessage(Message message){
              switch (message.what){
                  case 0:
                      noTask.setVisibility(View.GONE);
                      break;
                  case 1:
                      noTask.setVisibility(View.VISIBLE);
                      break;
              }
              super.handleMessage(message);
          }
        };
    }

    private void initView() {
        mList=(RecyclerView) findViewById(R.id.rv_task_list);
        noTask=(TextView) findViewById(R.id.tv_no_task);
        stopall=(TableRow) findViewById(R.id.tv_stop_all);
        cleanall=(TableRow) findViewById(R.id.tv_clean_all);
        setting=(TableRow) findViewById(R.id.tv_setting_all);
        close = (ImageView) findViewById(R.id.iv_task_close);

        close.setOnClickListener(this);
        cleanall.setOnClickListener(this);
        stopall.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_task_close:
                DownloadtaskActivity.this.finish();
                break;
            case R.id.tv_stop_all:
                okDownload.pauseAll();
                break;
            case R.id.tv_clean_all:
                okDownload.removeAll();
                mAdapter.updateData(DownloadAdapter.TYPE_ALL);
                mAdapter.notifyDataSetChanged();
                new Thread(runnable).start();
                new Thread(runnable2).start();
                break;
            case R.id.tv_setting_all:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_checkbox,(ViewGroup) findViewById(R.id.dialog_check));

        final CheckBoxSample checkBox1 = (CheckBoxSample) dialog.findViewById(R.id.check1);
        final CheckBoxSample checkBox2 = (CheckBoxSample) dialog.findViewById(R.id.check2);
        final CheckBoxSample checkBox3 = (CheckBoxSample) dialog.findViewById(R.id.check3);
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox1.toggle();
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                OkDownload.getInstance().getThreadPool().setCorePoolSize(1);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox2.toggle();
                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                OkDownload.getInstance().getThreadPool().setCorePoolSize(2);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox3.toggle();
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
            }
        });
        builder.setTitle("任务设置");
        builder.setMessage("原始地址线程数(同时下载任务量)：");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setNeutralButton("默认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkDownload.getInstance().getThreadPool().setCorePoolSize(1);
                T.show(DownloadtaskActivity.this,"默认同时进行1个任务",1);
            }
        });
        builder.setView(dialog);
        AlertDialog dialog1 = builder.create();
        dialog1.show();
        dialog1.getWindow().setLayout(450, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            if (mAdapter.getItemCount()>0){
                message.what=0;
            }else {
                message.what=1;
            }
            handler.sendMessage(message);
        }
    };

    @Override
    public void onAllTaskEnd() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        mAdapter.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Message message = AppFragment.handler.obtainMessage();
            message.what=1;
            AppFragment.handler.sendMessage(message);
        }
    };
}

package com.example.myapplication.ui.Update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UpdateListAdapter;
import com.example.myapplication.baen.LocalApp;
import com.example.myapplication.base.LoadFragment;
import com.example.myapplication.receiver.RefreshManager;
import com.example.myapplication.service.APIService;
import com.example.myapplication.ui.Activity.download.DownloadtaskActivity;
import com.example.myapplication.utils.database.SQLiteManager;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.widget.anims.LandingAnimator;
import com.example.myapplication.widget.anims.ScaleInAnimationAdapter;
import com.example.myapplication.widget.listview.LoadingDialog;
import com.example.okgo.OkGo;
import com.example.okgo.db.DownloadManager;
import com.example.okgo.request.GetRequest;
import com.example.service.OkDownload;
import com.example.service.download.DownloadTask;
import com.example.service.download.LogDownloadListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link LoadFragment} subclass.
 * 更新页面
 */

public class UpdateFragment extends LoadFragment implements RefreshManager.RefreshInterface{

    @BindView(R.id.rv_update_list) RecyclerView recyclerView;
    @BindView(R.id.update_refresh_layout) SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_down_num)
    TextView download_num;

    private UpdateListAdapter adapter;
    private Context context;
    private List<LocalApp> mList = new ArrayList<>();
    private LocalApp localApp;
    private List<DownloadTask> values;
    public static Handler handler;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    public void initViews(View view) {
        context = getActivity();
        handler = new Handler(){   //刷新界面当前下载个数
            public void handleMessage(Message message){
                switch (message.what){
                    case 1:
                        showDownloadNum();
                        break;
                    case 2://删除已更新应用信息
                        String num = message.obj+"";
                        SQLiteManager.getInstance().init(context,"updateApp");
                        SQLiteManager.getInstance().registerTable(localApp);
                        SQLiteManager.getInstance().open();
                        localApp.deleteOneByItem("url",num);
                        SQLiteManager.getInstance().close();
                        break;
                }
                super.handleMessage(message);
            }
        };
        initSql(context);
        initView();

        /**下拉刷新数据**/
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                SQLiteManager.getInstance().init(context,"updateApp");
                SQLiteManager.getInstance().registerTable(localApp);
                SQLiteManager.getInstance().read();
                mList = localApp.queryUpdate();
                adapter.notifyDataSetChanged();
                if (mList.size()>0){
                   mRefreshLayout.finishRefresh();
                }
            }
        });
        showDownloadNum();
    }
    /**动态更新正在下载个数**/
    private void showDownloadNum() {
        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        int num = values.size();
        if (num==0){
            download_num.setVisibility(View.INVISIBLE);
        }else {
            download_num.setVisibility(View.VISIBLE);
            download_num.setText(num + "");
        }
    }

    /**获取本地更新数据**/
    private void initSql(Context context) {
        localApp = new LocalApp();
        SQLiteManager.getInstance().init(context,"updateApp");
        SQLiteManager.getInstance().registerTable(localApp);
        SQLiteManager.getInstance().read();
        mList = localApp.queryUpdate();
    }

    private void initView() {
        LoadingDialog.showDialogForLoading(getActivity());
        //需要更新列表
        adapter = new UpdateListAdapter(context,mList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        adapter.notifyDataSetChanged();
        if (mList.size()>0)
            LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void loadData(Bundle savedInstanceState) {
        //mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.fab_update,R.id.rl_bar_download})
    void onClick(View view){
        switch (view.getId()){
            case R.id.fab_update:
                showDialog();
                break;
            case R.id.rl_bar_download:
                startActivity(new Intent(getActivity(), DownloadtaskActivity.class));
                break;
        }
    }
    /**弹出框提示更新**/
    private void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("一键更新").setIcon(R.drawable.ic_sync_24dp).
                setMessage("是否更新全部软件").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (LocalApp localApp :mList) {
                    String urls= APIService.APP_UPDATE_URL+localApp.getUrl();
                    GetRequest<File> request = OkGo.<File>get(urls).headers("aaa", "111")
                            .params("bbb", "222");

                    //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                    OkDownload.request(urls, request).save()
                            .register(new LogDownloadListener())
                            .start();
                    UpdateListAdapter.holder.download.setText("已在队列");
                    UpdateListAdapter.holder.download.setEnabled(false);
                    adapter.notifyDataSetChanged();
                    T.show(getActivity(),"所有更新任务已开始",1);
                    new Thread(UpdateListAdapter.runnable).start();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(700,200);
    }
    @Override
    public void onRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadBitMap(boolean isLoad) {

    }
}

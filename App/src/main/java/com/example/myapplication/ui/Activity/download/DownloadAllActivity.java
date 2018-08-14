package com.example.myapplication.ui.Activity.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.library.ProgressButton.circular.CircularProgressButton;
import com.example.myapplication.R;
import com.example.myapplication.baen.Apps;
import com.example.myapplication.service.APIService;
import com.example.myapplication.ui.Apps.MVP.AppPresenter;
import com.example.myapplication.ui.Apps.MVP.AppView;
import com.example.myapplication.utils.helper.ImageLoaderUtils;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.widget.anims.LandingAnimator;
import com.example.myapplication.widget.anims.ScaleInAnimationAdapter;
import com.example.myapplication.widget.listview.LoadingDialog;
import com.example.okgo.OkGo;
import com.example.okgo.db.DownloadManager;
import com.example.okgo.model.Progress;
import com.example.okgo.request.GetRequest;
import com.example.service.OkDownload;
import com.example.service.download.LogDownloadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/7/14 09:55
 * 4 * Desc:一键下载页面
 */

public class DownloadAllActivity extends AppCompatActivity implements AppView{

    private RecyclerView listview;

    private Button downall;

    private LinearLayout back;

    private DownAllAdapter adapter;

    private List<Apps.message> mlist =new ArrayList<>();

    private Context context;
    private AppPresenter presenter;
    private int start = 0;
    private int count = 0;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_all);
        downall=(Button)findViewById(R.id.bt_downall);
        back=(LinearLayout)findViewById(R.id.all_back);
        listview=(RecyclerView)findViewById(R.id.rv_downall_list);
        context=this;
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ApkDownload/");
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        initView();
        initClick();
    }

    private void initClick() {
        downall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Apps.message message :mlist) {
                    String urls=APIService.APP_APK_URL+message.getUrls();
                    GetRequest<File> request = OkGo.<File>get(urls).headers("aaa", "111")
                            .params("bbb", "222");

                    //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                    OkDownload.request(urls, request).save()
                            .register(new LogDownloadListener())
                            .start();
                    adapter.notifyDataSetChanged();
                    T.show(DownloadAllActivity.this,"所有任务开始下载",1);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAllActivity.this.finish();
            }
        });
    }

    private void initView() {
        presenter = new AppPresenter(this,context);
        presenter.loadApps(start,count,type);
        LoadingDialog.showDialogForLoading(this);
        //从数据库中恢复数据
        List<Progress> progressList = DownloadManager.getInstance().getAll();
        OkDownload.restore(progressList);
        adapter = new DownAllAdapter(context,mlist);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        listview.setLayoutManager(manager);
        listview.setItemAnimator(new LandingAnimator());
        listview.setAdapter(new ScaleInAnimationAdapter(adapter));
    }

    @Override
    public void returnAppMessage(List<Apps.message> messages) {
        if (messages.size() !=0){
            mlist.clear();
            mlist.addAll(messages);
            System.out.println("初次加载数据集大小："+mlist.size());
            adapter.notifyDataSetChanged();
        }
        if (mlist.size()>0){
            LoadingDialog.cancelDialogForLoading();
        }
    }

    @Override
    public void dismiss() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**创建适配器**/

    public class DownAllAdapter extends RecyclerView.Adapter<DownAllAdapter.ListViewHolder> {

        private final Context context;
        private final LayoutInflater inflater;
        private List<Apps.message> list;

        public DownAllAdapter(Context context,  List<Apps.message> list) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public DownAllAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DownAllAdapter.ListViewHolder(inflater.inflate(R.layout.item_apps_message, parent, false));
        }

        @Override
        public void onBindViewHolder(DownAllAdapter.ListViewHolder holder, int position) {
            Apps.message message = list.get(position);
            holder.bind(message);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            private TextView app_name;
            private TextView app_label;
            private ImageView app_icon;
            private CircularProgressButton download;
            private Apps.message message;

            public ListViewHolder(View itemView) {
                super(itemView);
                app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
                app_label = (TextView) itemView.findViewById(R.id.tv_app_label);
                app_icon = (ImageView) itemView.findViewById(R.id.iv_apps_icon);
                download = (CircularProgressButton) itemView.findViewById(R.id.bt_apps_download);
            }
            public void  bind(Apps.message message){
                this.message=message;
                String icon_url = APIService.APP_PICTURE_URL + message.getIcon();
                final String urls=APIService.APP_APK_URL+message.getUrls();
                if (OkDownload.getInstance().getTask(urls) != null) {
                    download.setText("已在队列");
                    download.setEnabled(false);
                } else {
                    download.setText("下载");
                    download.setEnabled(true);
                }
                app_name.setText(message.getName());
                app_label.setText(message.getLabel());
                ImageLoaderUtils.display(context, app_icon, icon_url);
                download.setIndeterminateProgressMode(true);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetRequest<File> request = OkGo.<File>get(urls).headers("aaa", "111")//
                                .params("bbb", "222");
                        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                        OkDownload.request(urls, request).save().register(new LogDownloadListener())//
                                .start();
                        adapter.notifyDataSetChanged();
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.show(context,"当前页不支持浏览详情",1);
                    }
                });
            }
        }
    }

}

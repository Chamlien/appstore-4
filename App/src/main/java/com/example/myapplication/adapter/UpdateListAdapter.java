package com.example.myapplication.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.library.ProgressButton.circular.CircularProgressButton;
import com.example.myapplication.R;
import com.example.myapplication.baen.LocalApp;
import com.example.myapplication.service.APIService;
import com.example.myapplication.ui.Update.UpdateFragment;
import com.example.myapplication.utils.helper.ImageLoaderUtils;
import com.example.myapplication.utils.helper.TimeUtil;
import com.example.okgo.OkGo;
import com.example.okgo.model.Progress;
import com.example.okgo.request.GetRequest;
import com.example.service.OkDownload;
import com.example.service.download.DownloadListener;
import com.example.service.download.DownloadTask;
import com.example.service.download.LogDownloadListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C),宁波瑞泽西医疗科技有限公司
 * Author: dell 许格
 * Date: 2018/7/4 11:02
 * History:
 * desc:
 */
public class UpdateListAdapter extends RecyclerView.Adapter<UpdateListAdapter.ListViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<LocalApp> list;
    private String PATH = Environment.getExternalStorageDirectory().getPath() + "/ApkDownload/";
    private DownloadTask task;
    String icon_url;
    public static ListViewHolder holder;
    private String apkName;

    public UpdateListAdapter(Context context, List<LocalApp> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ListViewHolder(inflater.inflate(R.layout.item_update_download, parent, false));

    }

    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        final LocalApp localApp = list.get(position);
        this.holder=holder;

        OkDownload.getInstance().setFolder(PATH);
        OkDownload.getInstance().getThreadPool().setCorePoolSize(1);

        icon_url = APIService.APP_PICTURE_URL + localApp.getIcons();

        if (task!=null) {
            holder.refresh(task.progress);
        }
        holder.bind(localApp);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.download.getProgress() == 0) {
                        holder.start(APIService.APP_UPDATE_URL+localApp.getUrl(),holder);
                }else if(holder.download.getProgress()==100){
                    Intent intent = new Intent();
                    PackageManager packageManager = context.getPackageManager();
                    intent = packageManager.getLaunchIntentForPackage(localApp.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                    context.startActivity(intent);
                }else if (holder.download.getProgress() == -1){
                    holder.start(APIService.APP_UPDATE_URL+localApp.getUrl(),holder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView app_name;
        private TextView app_time;
        private TextView app_update;
        private TextView app_function;
        private ImageView app_icon;
        public CircularProgressButton download;
        private DownloadTask task;
        private LocalApp message;

        public ListViewHolder(View itemView) {
            super(itemView);

            app_name = (TextView) itemView.findViewById(R.id.tv_update_name);
            app_time = (TextView) itemView.findViewById(R.id.tv_update_time);
            app_update=(TextView) itemView.findViewById(R.id.tv_update_version);
            app_function=(TextView)itemView.findViewById(R.id.tv_update_function);
            app_icon = (ImageView) itemView.findViewById(R.id.iv_update_icon);
            download = (CircularProgressButton) itemView.findViewById(R.id.bt_update_download);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void start(String url,ListViewHolder holder){
            new Thread(runnable).start();
            GetRequest<File> request = OkGo.<File>get(url).headers("aaa", "111").params("bbb", "222");
            //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
            task=OkDownload.request(url, request).save()
                    .register(new ListDownloadListener(holder))
                    .register(new LogDownloadListener());
            task.start();
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
                    break;
            }
            refresh(task.progress);
        }

        public void bind(LocalApp localApp){
            this.message=localApp;
            Date newDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                newDate = sdf.parse(message.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String time=TimeUtil.getTimeFormatText(newDate);
            app_name.setText(message.getAppName());
            app_time.setText(time);
            app_update.setText("版本 "+message.getVersion()+" · "+message.getSize()+"MB");
            app_function.setText("- "+message.getFunction());
            ImageLoaderUtils.display(context, app_icon, icon_url);
            download.setIndeterminateProgressMode(true);
            if (OkDownload.getInstance().getTask(APIService.APP_UPDATE_URL+localApp.getUrl()) != null) {
                download.setText("已更新");
                download.setEnabled(false);
            } else {
                download.setText("更新");
                download.setEnabled(true);
            }
        }

        public void refresh(Progress progress) {
            switch (progress.status) {
                case Progress.NONE:
                    download.setProgress(0);
                    download.setText("更新");
                    break;
                case Progress.PAUSE:
                    download.setProgress(0);
                    download.setText("继续");
                    break;
                case Progress.ERROR:
                    download.setText("出错");
                    download.setProgress(-1);
                    break;
                case Progress.WAITING:
                    download.setProgress(1);
                    break;
                case Progress.FINISH:
                    download.setProgress(100);
                    SuccessProgress(download);
                    download.setText("正在安装");
                    apkName = progress.fileName;
                    new Thread(runnable2).start();
                    break;
                case Progress.LOADING:
                    break;
            }
        }
    }
    private void SuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1000);
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

    public static class ListDownloadListener extends DownloadListener {

        private ListViewHolder holder;

        public ListDownloadListener(ListViewHolder holder) {
            super(holder);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
                holder.refresh(progress);
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    public static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = UpdateFragment.handler.obtainMessage();
            message.what=1;
            UpdateFragment.handler.sendMessage(message);
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Message message = UpdateFragment.handler.obtainMessage();
            message.what=2;
            message.obj=apkName;
            UpdateFragment.handler.sendMessage(message);
        }
    };
}

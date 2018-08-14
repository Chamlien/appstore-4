package com.example.myapplication.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import com.example.myapplication.baen.Apps;
import com.example.myapplication.service.APIService;
import com.example.myapplication.ui.Apps.AppsActivity;
import com.example.myapplication.ui.Apps.Fragment.AppFragment;
import com.example.myapplication.utils.helper.ImageLoaderUtils;
import com.example.okgo.OkGo;
import com.example.okgo.model.Progress;
import com.example.okgo.request.GetRequest;
import com.example.service.OkDownload;
import com.example.service.download.DownloadListener;
import com.example.service.download.DownloadTask;
import com.example.service.download.LogDownloadListener;

import java.io.File;
import java.util.List;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: AppsListAdapter
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 15:42
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ListViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<Apps.message> list;
    private String PATH = Environment.getExternalStorageDirectory().getPath() + "/ApkDownload/";
    private DownloadTask task;
    String icon_url;
    String urls;

    public AppsListAdapter(Context context, List<Apps.message> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ListViewHolder(inflater.inflate(R.layout.item_apps_message, parent, false));

    }

    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        final Apps.message message = list.get(position);

        OkDownload.getInstance().setFolder(PATH);
        OkDownload.getInstance().getThreadPool().setCorePoolSize(1);

        icon_url = APIService.APP_PICTURE_URL + message.getIcon();
        urls=APIService.APP_APK_URL+message.getUrls();

        if (task!=null) {
            holder.refresh(task.progress);
        }
        holder.bind(message);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InApplication(context, message.getPackagename())){
                    Intent intent = new Intent();
                    PackageManager packageManager = context.getPackageManager();
                    intent = packageManager.getLaunchIntentForPackage(message.getPackagename());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                    context.startActivity(intent);
                }else {
                    if (holder.download.getProgress() == 0) {
                        holder.start(APIService.APP_APK_URL+message.getUrls(),holder);
                    }else if (holder.download.getProgress() == -1){
                        holder.start(APIService.APP_APK_URL+message.getUrls(),holder);
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppsActivity.class);
                intent.putExtra("icon", APIService.APP_PICTURE_URL + message.getIcon());
                intent.putExtra("name", message.getName());
                intent.putExtra("label", message.getLabel());
                intent.putExtra("function", message.getNewfunction());
                intent.putExtra("version", message.getVersion());
                intent.putExtra("video", message.getVideo());
                intent.putExtra("developers", message.getDevelopers());
                intent.putExtra("difficulty", message.getDifficulty());
                intent.putExtra("size", message.getSize());
                intent.putExtra("urls", message.getUrls());
                intent.putExtra("history", message.getOldfunction());
                intent.putExtra("type", message.getCategory());
                intent.putExtra("image1", message.getImage1());
                intent.putExtra("image2", message.getImage2());
                intent.putExtra("image3", message.getImage3());
                intent.putExtra("time",message.getTime());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView app_name;
        private TextView app_label;
        private TextView down_progress;
        private ImageView app_icon;
        private CircularProgressButton download;
        private DownloadTask task;
        private String tag;
        private Apps.message message;

        public ListViewHolder(View itemView) {
            super(itemView);

            app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            app_label = (TextView) itemView.findViewById(R.id.tv_app_label);
            down_progress=(TextView) itemView.findViewById(R.id.down_progress);
            app_icon = (ImageView) itemView.findViewById(R.id.iv_apps_icon);
            download = (CircularProgressButton) itemView.findViewById(R.id.bt_apps_download);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void start(String url,ListViewHolder holder){
            GetRequest<File> request = OkGo.<File>get(url).headers("aaa", "111").params("bbb", "222");
            //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
            task=OkDownload.request(url, request).save()
                    .register(new ListDownloadListener(tag, holder))
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
            new Thread(runnable).start();
        }

        public void bind(Apps.message message){
            this.message=message;
            app_name.setText(message.getName());
            app_label.setText(message.getLabel());
            ImageLoaderUtils.display(context, app_icon, icon_url);
            download.setIndeterminateProgressMode(true);

            if (InApplication(context, message.getPackagename())) {
                download.setProgress(100);
                download.setText("打开");
            } else {
                download.setProgress(0);
                download.setText("下载");
            }
        }

        public void refresh(Progress progress) {
            switch (progress.status) {
                case Progress.NONE:
                    down_progress.setText("已停止");
                    download.setProgress(0);
                    download.setText("下载");
                    break;
                case Progress.PAUSE:
                    down_progress.setText("暂停中");
                    download.setProgress(0);
                    download.setText("继续");
                    break;
                case Progress.ERROR:
                    down_progress.setText("下载出错");
                    download.setText("出错");
                    download.setProgress(-1);
                    break;
                case Progress.WAITING:
                    down_progress.setText("等待中");
                    download.setProgress(1);
                    break;
                case Progress.FINISH:
                    /*try {
                      RootTools.sendShell("pm install /storage/emulated/0/ApkDownload/"+progress.fileName, 20000);//偷偷安装
                      //RootTools.sendShell("rm /storage/extsd/apk/test.apk", 5000);//偷偷删除
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                      e.printStackTrace();
                    }catch (RootToolsException e) {
                            // TODO Auto-generated catch block
                      e.printStackTrace();
                    } catch (TimeoutException e) {
                            // TODO Auto-generated catch block
                      e.printStackTrace();
                    }*/
                    down_progress.setText("下载完成");
                    download.setProgress(100);
                    SuccessProgress(download);
                    download.setText("打开");
                    break;
                case Progress.LOADING:
                    break;
            }
            down_progress.setText((int)(progress.fraction * 100)+"%");
        }
    }

    public boolean InApplication(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        //获取系统中安装的应用包的信息

        List<PackageInfo> listPackageInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < listPackageInfo.size(); i++) {
            if (listPackageInfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
            return false;
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

    private class ListDownloadListener extends DownloadListener {

        private ListViewHolder holder;

        ListDownloadListener(Object tag, ListViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
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
            Message message = AppFragment.handler.obtainMessage();
            message.what=1;
            AppFragment.handler.sendMessage(message);
        }
    };
}

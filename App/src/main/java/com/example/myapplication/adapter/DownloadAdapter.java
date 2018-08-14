package com.example.myapplication.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.widget.circleprogress.HorizontalProgressBarWithNumber;
import com.example.okgo.db.DownloadManager;
import com.example.okgo.model.Progress;
import com.example.service.OkDownload;
import com.example.service.download.DownloadListener;
import com.example.service.download.DownloadTask;
import com.example.service.download.LogDownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: DownloadAdapter
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/27 13:25
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10 下载任务适配器
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ListViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public DownloadAdapter(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING)
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_task_download, parent, false);
        return new ListViewHolder(view);
    }

    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final DownloadTask task = values.get(position);
        String tag = createTag(task);
        task.register(new ListDownloadListener(tag, holder))//
                .register(new LogDownloadListener());
        holder.setTag(tag);
        holder.setTask(task);
        holder.bind();
        holder.refresh(task.progress);
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.start();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.remove();
                updateData(type);
            }
        });
    }

    private String createTag(DownloadTask task) {
        return type + "_" + task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView start;
        private ImageView delete;
        private ImageView icon;
        private TextView speed;
        private TextView name;
        private TextView filesize;
        private HorizontalProgressBarWithNumber hprogress;
        private DownloadTask task;
        private String tag;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_task_icon);
            name = (TextView) itemView.findViewById(R.id.task_name);
            speed = (TextView) itemView.findViewById(R.id.speed);
            filesize = (TextView) itemView.findViewById(R.id.fileSize);
            start = (ImageView) itemView.findViewById(R.id.iv_stop_task);
            delete = (ImageView) itemView.findViewById(R.id.iv_delete_task);
            hprogress = (HorizontalProgressBarWithNumber) itemView.findViewById(R.id.h_progressBar);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;

            name.setText(progress.fileName);
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
            filesize.setText(currentSize + "/" + totalSize);
            switch (progress.status) {
                case Progress.NONE:
                    speed.setText("停止");
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_24dp));
                    break;
                case Progress.PAUSE:
                    speed.setText("暂停中");
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    break;
                case Progress.ERROR:
                    speed.setText("下载出错");
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_error_black_24dp));
                    break;
                case Progress.WAITING:
                    speed.setText("等待中");
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wait_24dp));
                    break;
                case Progress.FINISH:
                    speed.setText("下载完成");
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_24dp));
                    String path = "/storage/emulated/0/ApkDownload/"+ progress.fileName;
                    new installTask().execute(path);
                    break;
                case Progress.LOADING:
                    String speeds = Formatter.formatFileSize(context, progress.speed);
                    speed.setText(String.format("%s/s", speeds));
                    start.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_24dp));
                    break;
            }
            hprogress.setMax(100);
            hprogress.setProgress((int) (progress.fraction * 100));
        }

        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
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
            refresh(progress);
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
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
            Toast.makeText(context, "正在安装:" + progress.fileName+"请稍后...", Toast.LENGTH_SHORT).show();
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
            holder.refresh(progress);
        }
    }

    class installTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            install(strings[0]);
            return null;
        }
    }

    //静默安装（如有已安装则覆盖安装）方法
    protected void install(String filepath) {
        Process process = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 请求root
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            // 调用安装
            out.write(("pm install -r " + filepath + "\n").getBytes());
            in = process.getInputStream();
            int len = 0;
            byte[] bs = new byte[256];
            while (-1 != (len = in.read(bs))) {
                String state = new String(bs, 0, len);
                if (state.equals("success\n")) {
                    //安装成功后的操作
                    T.show(context,"安装完成",1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

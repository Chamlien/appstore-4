package com.example.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.baen.Updated;
import com.example.myapplication.service.APIService;
import com.example.myapplication.utils.helper.ImageLoaderUtils;
import com.example.myapplication.utils.helper.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/8/6 15:20
 * History:
 * desc:已更新列表adapter
 **/
public class UpdatedAdapter extends RecyclerView.Adapter<UpdatedAdapter.ListViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private List<Updated> list;

    public UpdatedAdapter(Context context, List<Updated> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ListViewHolder(inflater.inflate(R.layout.item_update_download, parent, false));

    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Updated updated = list.get(position);
        String icon_url = APIService.APP_PICTURE_URL + updated.getIcons();
        ImageLoaderUtils.display(context, holder.app_icon, icon_url);
        Date newDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            newDate = sdf.parse(updated.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time= TimeUtil.getTimeFormatText(newDate);
        holder.app_name.setText(updated.getAppName());
        holder.app_time.setText(time);
        holder.app_update.setText("版本 "+updated.getVersion()+" · "+updated.getSize()+"MB");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView app_name;
        private TextView app_time;
        private TextView app_update;
        private ImageView app_icon;

        public ListViewHolder(View itemView) {
            super(itemView);

            app_name = (TextView) itemView.findViewById(R.id.tv_update_name);
            app_time = (TextView) itemView.findViewById(R.id.tv_update_time);
            app_update = (TextView) itemView.findViewById(R.id.tv_update_version);
            app_icon = (ImageView) itemView.findViewById(R.id.iv_update_icon);
        }
    }
}

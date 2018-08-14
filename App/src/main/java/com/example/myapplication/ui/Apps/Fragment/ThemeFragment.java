package com.example.myapplication.ui.Apps.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AppsListAdapter;
import com.example.myapplication.baen.Apps;
import com.example.myapplication.base.BaseFragment;
import com.example.myapplication.ui.Apps.MVP.AppPresenter;
import com.example.myapplication.ui.Apps.MVP.AppView;
import com.example.myapplication.widget.anims.LandingAnimator;
import com.example.myapplication.widget.anims.ScaleInAnimationAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 主题页面
 */
public class ThemeFragment extends BaseFragment implements AppView{

    private Context context;

    RecyclerView listview;

    private AppsListAdapter adapter;

    List<Apps.message> mlist =new ArrayList<>();

    private AppPresenter presenter;
    private int start = 0;//开始
    private int count = 100;//结束
    private int type = 2;//类型（1：游戏；2：主题）
    private SmartRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        listview=(RecyclerView)view.findViewById(R.id.rv_theme_list);
        mRefreshLayout=(SmartRefreshLayout)view.findViewById(R.id.theme_refresh_layout);
        initView();
        return view;
    }
    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void initView() {

        context =getActivity();
        presenter = new AppPresenter(this,context);
        presenter.loadApps(start,count,type);
        adapter = new AppsListAdapter(context,mlist);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        listview.setLayoutManager(manager);
        listview.setItemAnimator(new LandingAnimator());
        listview.setAdapter(new ScaleInAnimationAdapter(adapter));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadApps(start,count,type);
            }
        });
    }

    /**
     * 获取主题数据
     * @param messages
     */
    @Override
    public void returnAppMessage(List<Apps.message> messages) {
        if (messages.size() !=0){
            mlist.clear();
            mlist.addAll(messages);
            System.out.println("初次加载数据集大小："+mlist.size());
            adapter.notifyDataSetChanged();
            mRefreshLayout.finishRefresh();
         }
    }

    @Override
    public void dismiss() {
        mRefreshLayout.finishRefresh();//结束刷新
    }
}

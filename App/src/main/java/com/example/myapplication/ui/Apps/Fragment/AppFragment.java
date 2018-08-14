package com.example.myapplication.ui.Apps.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.SearchView.MaterialSearchView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AppsViewPagerAdapter;
import com.example.myapplication.base.BaseFragment;
import com.example.myapplication.ui.Activity.download.DownloadAllActivity;
import com.example.myapplication.ui.Activity.download.DownloadtaskActivity;
import com.example.myapplication.ui.Activity.other.LoginActivity;
import com.example.myapplication.widget.cardviews.SlidingTab;
import com.example.okgo.db.DownloadManager;
import com.example.service.OkDownload;
import com.example.service.download.DownloadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/01 10:22
 * 4 * Desc:App页面（包含游戏和主题两个页面）
 */

public class AppFragment extends BaseFragment {

    @BindView(R.id.tl_apps_net)
    SlidingTab mTlAPPsNet;

    @BindView(R.id.vp_apps_net)
    ViewPager mVpAPPsNet;

    @BindView(R.id.fab_apps)
    FloatingActionButton fab;

    @BindView(R.id.iv_bar_download)
    ImageView download;

    @BindView(R.id.iv_bar_person)
    ImageView person;

    @BindView(R.id.et_search_name)
    EditText search;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.tv_title)
    TextView themeTitle;

    @BindView(R.id.login_output)
    TextView loginOut;

    @BindView(R.id.tv_app_down_num)
    TextView down_num;

    private String[] title = {"游戏", "主题"};

    private Context mContext;

    private AppsViewPagerAdapter appsViewPagerAdapter;

    private boolean isLogin=false;

    OnHeadlineSelectedListener mCallback;

    private List<DownloadTask> values;

    public static Handler handler;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_app;
    }

    @Override
    protected void initView() {
        mContext=getActivity();
        checkLogin();

        //更新UI界面
        handler = new Handler(){   //刷新界面当前下载个数
            public void handleMessage(Message message){
                switch (message.what){
                    case 1:
                        showDownloadNum();
                        break;
                    case 2:
                        checkLogin();
                        break;
                }
                super.handleMessage(message);
            }
        };

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogin==true){
                    mCallback.onArticleSelected(3);
                }else if (isLogin==false) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.openSearch();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DownloadtaskActivity.class));
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, false);
            }
        });

        searchView.adjustTintAlpha(0.8f);

        searchView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(mContext, "Long clicked position: " + i, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchView.setOnVoiceClickedListener(new MaterialSearchView.OnVoiceClickedListener() {
            @Override
            public void onVoiceClicked() {
                Toast.makeText(mContext, "Voice clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        loadApps();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"一键下载必备软件",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(DownloadAllActivity.class);
                            }
                        })
                        .show();
            }
        });

        showDownloadNum();
    }

    private void showDownloadNum() {//显示当前正在下载的个数
        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        int num = values.size();
        if (num==0){
            down_num.setVisibility(View.INVISIBLE);
        }else {
            down_num.setVisibility(View.VISIBLE);
            down_num.setText(num + "");
        }
    }

    /**
     * 检查用户是否登录
     */

    private void checkLogin() {
        File file = new File(getActivity().getCacheDir(),"Login.txt");
        if(file.exists() && file.length() > 0){
            try {
                //读流
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String QQLogin = br.readLine();
                //帐号密码用##分隔，所以用##切割
                String user_name = QQLogin.split("##")[0];
                String pwd = QQLogin.split("##")[1];
                //并将缓存的帐号密码显现
                if (pwd.equals("admin")){
                    person.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_account_white_24dp));
                    loginOut.setVisibility(View.GONE);
                    isLogin=true;
                }else {
                    person.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_account_24dp));
                    loginOut.setVisibility(View.VISIBLE);
                    isLogin=false;
                }
                fis.close();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadApps() {
        List<BaseFragment> mappFragmenList;
        mappFragmenList=createListFragments();
        if (appsViewPagerAdapter == null) {
            appsViewPagerAdapter = new AppsViewPagerAdapter(getActivity().getSupportFragmentManager(), mappFragmenList, Arrays.asList(title));
        } else {
            //刷新fragment
            appsViewPagerAdapter.setFragments(getActivity().getSupportFragmentManager(), mappFragmenList, Arrays.asList(title));
        }
        mVpAPPsNet.setAdapter(appsViewPagerAdapter);
        int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        mVpAPPsNet.setPageMargin(pageMargin);
        mTlAPPsNet.setViewPager(mVpAPPsNet);
        setTabsValue();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode ==getActivity().RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 配置切换条样式
     */

    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        mTlAPPsNet.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mTlAPPsNet.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mTlAPPsNet.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        mTlAPPsNet.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        mTlAPPsNet.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        mTlAPPsNet.setTextColor(Color.parseColor("#ffffff"));
        mTlAPPsNet.setIndicatorColor(Color.parseColor("#ffffff"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mTlAPPsNet.setSelectedTextColor(Color.parseColor("#ffffff"));
        // 取消点击Tab时的背景色
        mTlAPPsNet.setTabBackground(0);
    }

    private  List<BaseFragment> createListFragments() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new GameFragment());
        list.add(new ThemeFragment());
        return list;
    }

    // Container Activity must implement this interface 接口
    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

}

package com.example.myapplication.adapter;

import android.support.v4.app.FragmentManager;


import com.example.myapplication.base.BaseFragment;
import com.example.myapplication.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by lvr on 2017/2/6.
 */

public class AppsViewPagerAdapter extends BaseFragmentAdapter {
    public AppsViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> mTitles) {
        super(fm, fragmentList, mTitles);
    }

    public AppsViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }
}

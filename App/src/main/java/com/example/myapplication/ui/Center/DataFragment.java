package com.example.myapplication.ui.Center;

import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseFragment;

import butterknife.OnClick;


public class DataFragment extends BaseFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_data;
    }

    @Override
    protected void initView() {

    }

    @OnClick()
    void onClick(View v){
        switch (v.getId()){
        }
    }
}

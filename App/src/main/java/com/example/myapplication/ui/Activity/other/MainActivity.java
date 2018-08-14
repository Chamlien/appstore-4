package com.example.myapplication.ui.Activity.other;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.library.BottomNavigation.BottomNavigationItem;
import com.example.library.BottomNavigation.BottomNavigationView;
import com.example.library.BottomNavigation.OnBottomNavigationItemClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ui.Apps.Fragment.AppFragment;
import com.example.myapplication.ui.Center.CenterFragment;
import com.example.myapplication.ui.Manage.ManageFragment;
import com.example.myapplication.ui.Update.UpdateFragment;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/7/01 08:53
 * 4 * Desc:主界面（绑定四个Fragment）
 */

public class MainActivity extends AppCompatActivity implements AppFragment.OnHeadlineSelectedListener{

    private BottomNavigationView bottomNavigationView;

    private Fragment AppFragment;

    private Fragment UpdateFragment;

    private Fragment ManageFragment;

    public Fragment CenterFragment;

    private boolean first=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //applyKitKatTranslucency();

        /**配置底部导航条
         * @param image 图片
         * @param color 颜色
         * @param name  名称
         * */
        int[] image = {R.drawable.ic_apps_24dp, R.drawable.ic_update_24dp, R.drawable.ic_settings_24dp, R.drawable.ic_person_24dp};
        int[] color = {ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.white)};

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.text_active));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.text_inactive));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.firstColor));
            bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_normal.ttf"));
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("应用", color[0], image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("更新", color[1], image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("管理", color[2], image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("我", color[3], image[3]);


        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        selectedImages(0);//app页面
                        break;
                    case 1:
                        selectedImages(1);//更新页面
                        break;
                    case 2:
                        if (first==false){
                            selectedImages(2);//管理页面
                            startActivity(new Intent(MainActivity.this,ScanningActivity.class));
                            first=true;
                        }else {
                            selectedImages(2);
                        }
                        break;
                    case 3:
                        selectedImages(3);//个人中心页面
                        break;
                }
            }
        });
        selectedImages(0);

    }

    /**
     * 设置选中
     *
     * @param i
     */
    private void selectedImages(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (i) {
            case 0:
                if (AppFragment == null) {
                    AppFragment = new AppFragment();
                    fragmentTransaction.add(R.id.fragment_navigation, AppFragment);
                } else {
                    fragmentTransaction.show(AppFragment);
                }
                break;
            case 1:
                if (UpdateFragment == null) {
                    UpdateFragment = new UpdateFragment();
                    fragmentTransaction.add(R.id.fragment_navigation, UpdateFragment);
                } else {
                    fragmentTransaction.show(UpdateFragment);
                }
                break;
            case 2:
                if (ManageFragment == null) {
                    ManageFragment = new ManageFragment();
                    fragmentTransaction.add(R.id.fragment_navigation, ManageFragment);
                } else {
                    fragmentTransaction.show(ManageFragment);
                }
                break;
            case 3:
                if (CenterFragment == null) {
                    CenterFragment = new CenterFragment();
                    fragmentTransaction.add(R.id.fragment_navigation, CenterFragment);
                } else {
                    fragmentTransaction.show(CenterFragment);
                }
                break;
            default:
                break;

        }
        fragmentTransaction.commit();
    }

    /**
     * 初始化隐藏所有Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (AppFragment != null) {
            fragmentTransaction.hide(AppFragment);
        }
        if (UpdateFragment != null) {
            fragmentTransaction.hide(UpdateFragment);
        }
        if (ManageFragment != null) {
            fragmentTransaction.hide(ManageFragment);
        }
        if (CenterFragment != null) {
            fragmentTransaction.hide(CenterFragment);
        }
    }

    /**
     * 禁用返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 调用返回值刷新UI底部按钮位置
     * @param position
     */
    @Override
    public void onArticleSelected(int position) {
        selectedImages(position);
        bottomNavigationView.getItem(position);
    }
}


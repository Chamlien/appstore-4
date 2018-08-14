package com.example.myapplication.ui.Manage;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.library.Clean.enums.QuickReturnType;
import com.example.library.Clean.listeners.QuickReturnListViewOnScrollListener;
import com.example.library.LoadingView.AnimatedCircleLoadingView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ClearMemoryAdapter;
import com.example.myapplication.baen.AppProcessInfo;
import com.example.myapplication.base.BaseSwipeBackActivity;
import com.example.myapplication.service.CoreService;
import com.example.myapplication.utils.systemUtils.StorageSize;
import com.example.myapplication.utils.systemUtils.StorageUtil;
import com.example.myapplication.utils.viewUtils.SystemBarTintManager;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.utils.helper.UIElementsHelper;
import com.example.myapplication.widget.textcounter.CounterView;
import com.example.myapplication.widget.textcounter.formatters.DecimalFormatter;
import com.example.myapplication.widget.waveview.WaveView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link } subclass.
 * 内存加速页面
 */

public class MemoryCleanActivity extends BaseSwipeBackActivity implements OnDismissCallback, CoreService.OnPeocessActionListener {

    long killAppmemory = 0;

    ListView mListView;

    WaveView mwaveView;

    RelativeLayout header;
    List<AppProcessInfo> mAppProcessInfos = new ArrayList<>();
    ClearMemoryAdapter mClearMemoryAdapter;

    CounterView textCounter;
    TextView sufix;
    public long Allmemory;

    LinearLayout bottom_lin;

    View mProgressBar;
    TextView mProgressBarText;
    LinearLayout back;

    Button clearButton;
    private static final int INITIAL_DELAY_MILLIS = 300;

    private CoreService mCoreService;
    StorageSize mStorageSize;
    AnimatedCircleLoadingView loadingView;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCoreService = ((CoreService.ProcessServiceBinder) service).getService();
            mCoreService.setOnActionListener(MemoryCleanActivity.this);
            mCoreService.scanRunProcess();
            //  updateStorageUsage();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCoreService.setOnActionListener(null);
            mCoreService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_clean);
        //applyKitKatTranslucency();
        initview();
        mClearMemoryAdapter = new ClearMemoryAdapter(mContext, mAppProcessInfos);
        mListView.setAdapter(mClearMemoryAdapter);
        bindService(new Intent(mContext, CoreService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
        int footerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.footer_height);
        mListView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, bottom_lin, footerHeight));
        textCounter.setAutoFormat(false);
        textCounter.setFormatter(new DecimalFormatter());
        textCounter.setAutoStart(false);
        textCounter.setIncrement(5f); // the amount the number increments at each time interval
        textCounter.setTimeInterval(50); // the time interval (ms) at which the text changes
    }

    private void initview() {


        mListView =(ListView)findViewById(R.id.listview);

        mwaveView =(WaveView)findViewById(R.id.wave_view);

        header =(RelativeLayout)findViewById(R.id.header);

        textCounter=(CounterView)findViewById(R.id.textCounter);

        sufix=(TextView)findViewById(R.id.sufix);

        bottom_lin =(LinearLayout)findViewById(R.id.bottom_lin);

        mProgressBar =(View)findViewById(R.id.progressBar);

        mProgressBarText =(TextView)findViewById(R.id.progressBarText);

        clearButton =(Button)findViewById(R.id.clear_button);

        loadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);

        back=(LinearLayout)findViewById(R.id.clean_ll);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClear();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            // mTintManager.setTintColor(0xF00099CC);

            mTintManager.setTintDrawable(UIElementsHelper
                    .getGeneralActionBarBackground(this));


        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {

    }

    @Override
    public void onScanStarted(Context context) {
        mProgressBarText.setText(R.string.scanning);
        showProgressBar(true);
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        mProgressBarText.setText(getString(R.string.scanning_m_of_n, current, max));
    }

    @Override
    public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
        mAppProcessInfos.clear();

        Allmemory = 0;
        for (AppProcessInfo appInfo : apps) {
            if (!appInfo.isSystem) {
                mAppProcessInfos.add(appInfo);
                Allmemory += appInfo.memory;
            }
        }


        refeshTextCounter();

        mClearMemoryAdapter.notifyDataSetChanged();
        showProgressBar(false);


        if (apps.size() > 0) {
            header.setVisibility(View.VISIBLE);
            bottom_lin.setVisibility(View.VISIBLE);


        } else {
            header.setVisibility(View.GONE);
            bottom_lin.setVisibility(View.GONE);
        }
//        mClearMemoryAdapter = new ClearMemoryAdapter(mContext,
//                apps);  mClearMemoryAdapter = new ClearMemoryAdapter(mContext,
//                apps);
//        swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mClearMemoryAdapter, MemoryCleanActivity.this));
//        swingBottomInAnimationAdapter.setAbsListView(mListView);
//        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
//
//        mListView.setAdapter(swingBottomInAnimationAdapter);
        //clearMem.setText("200M");


    }

    private void refeshTextCounter() {
        mwaveView.setProgress(20);
        mStorageSize = StorageUtil.convertStorageSize(Allmemory);
        textCounter.setStartValue(0f);
        textCounter.setEndValue(mStorageSize.value);
        sufix.setText(mStorageSize.suffix);
        //  textCounter.setSuffix(mStorageSize.suffix);
        textCounter.start();
    }

    @Override
    public void onCleanStarted(Context context) {

    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {

    }


    public void onClickClear() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startDeterminate();
        startPercentMockThread();

        for (int i = mAppProcessInfos.size() - 1; i >= 0; i--) {
            if (mAppProcessInfos.get(i).checked) {
                killAppmemory += mAppProcessInfos.get(i).memory;
                mCoreService.killBackgroundProcesses(mAppProcessInfos.get(i).processName);
                mAppProcessInfos.remove(mAppProcessInfos.get(i));
                mClearMemoryAdapter.notifyDataSetChanged();
            }
        }
        Allmemory = Allmemory - killAppmemory;

        if (Allmemory > 0) {
            refeshTextCounter();
        }
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i <= 100; i=i+2) {
                        Thread.sleep(50);
                        changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setPercent(percent);
                if (percent==100){
                    T.showLong(mContext, "共清理" + StorageUtil.convertStorage(killAppmemory) + "内存");
                    clearButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}

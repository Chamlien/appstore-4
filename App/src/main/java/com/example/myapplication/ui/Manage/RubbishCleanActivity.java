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
import android.text.format.Formatter;
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
import android.widget.Toast;

import com.example.library.Clean.enums.QuickReturnType;
import com.example.library.Clean.listeners.QuickReturnListViewOnScrollListener;
import com.example.myapplication.R;
import com.example.myapplication.adapter.RublishMemoryAdapter;
import com.example.myapplication.baen.CacheListItem;
import com.example.myapplication.base.BaseSwipeBackActivity;
import com.example.myapplication.service.CleanService;
import com.example.myapplication.utils.systemUtils.StorageSize;
import com.example.myapplication.utils.systemUtils.StorageUtil;
import com.example.myapplication.utils.viewUtils.SystemBarTintManager;
import com.example.myapplication.utils.helper.UIElementsHelper;
import com.example.myapplication.widget.textcounter.CounterView;
import com.example.myapplication.widget.textcounter.formatters.DecimalFormatter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link } subclass.
 * 垃圾清理页面
 */
public class RubbishCleanActivity extends BaseSwipeBackActivity implements OnDismissCallback , CleanService.OnActionListener {

    private CleanService mCleanerService;

    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned =false;

    ListView mListView;

    TextView mEmptyView;

    RelativeLayout header;

    CounterView textCounter;
    TextView sufix;

    View mProgressBar;
    TextView mProgressBarText;

    RublishMemoryAdapter rublishMemoryAdapter;

    List<CacheListItem> mCacheListItem = new ArrayList<>();

    LinearLayout bottom_lin;

    Button clearButton;

    LinearLayout rubbish;

    /**
     * 绑定服务，开启服务扫描本地垃圾
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanService.CleanServiceBinder) service).getService();
            mCleanerService.setOnActionListener(RubbishCleanActivity.this);

            //  updateStorageUsage();

            if (!mCleanerService.isScanning() && !mAlreadyScanned) {
                mCleanerService.scanCache();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish_clean);
        initview();
        applyKitKatTranslucency();
        int footerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.footer_height);

        mListView.setEmptyView(mEmptyView);
        rublishMemoryAdapter = new RublishMemoryAdapter(mContext, mCacheListItem);
        mListView.setAdapter(rublishMemoryAdapter);
        mListView.setOnItemClickListener(rublishMemoryAdapter);
        mListView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, bottom_lin, footerHeight));
        bindService(new Intent(mContext, CleanService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initview() {
        mListView =(ListView)findViewById(R.id.listview);
        mEmptyView=(TextView) findViewById(R.id.empty);
        header =(RelativeLayout) findViewById(R.id.header);
        textCounter =(CounterView) findViewById(R.id.textCounter);
        sufix =(TextView)findViewById(R.id.sufix);
        mProgressBar =(View)findViewById(R.id.progressBar);
        mProgressBarText =(TextView)findViewById(R.id.progressBarText);
        bottom_lin =(LinearLayout)findViewById(R.id.bottom_lin);
        clearButton =(Button)findViewById(R.id.clear_button);
        rubbish =(LinearLayout)findViewById(R.id.rubbish_ll);

        rubbish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
                    mAlreadyCleaned = false;
                    mCleanerService.cleanCache();
                }
            }
        });

    }

    @Override
    public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {

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
    public void onScanCompleted(Context context, List<CacheListItem> apps) {
        showProgressBar(false);
        mCacheListItem.clear();
        mCacheListItem.addAll(apps);
        rublishMemoryAdapter.notifyDataSetChanged();
        header.setVisibility(View.GONE);
        if (apps.size() > 0) {
            header.setVisibility(View.VISIBLE);
            bottom_lin.setVisibility(View.VISIBLE);

            long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;

            StorageSize mStorageSize = StorageUtil.convertStorageSize(medMemory);
            textCounter.setAutoFormat(false);
            textCounter.setFormatter(new DecimalFormatter());
            textCounter.setAutoStart(false);
            textCounter.setStartValue(0f);
            textCounter.setEndValue(mStorageSize.value);
            textCounter.setIncrement(5f); // the amount the number increments at each time interval
            textCounter.setTimeInterval(50); // the time interval (ms) at which the text changes
            sufix.setText(mStorageSize.suffix);
            //  textCounter.setSuffix(mStorageSize.suffix);
            textCounter.start();
        } else {
            header.setVisibility(View.GONE);
            bottom_lin.setVisibility(View.GONE);
        }

        if (!mAlreadyScanned) {
            mAlreadyScanned = true;

        }
    }

    @Override
    public void onCleanStarted(Context context) {
        if (isProgressBarVisible()) {
            showProgressBar(false);
        }

        if (!RubbishCleanActivity.this.isFinishing()) {
            showDialogLoading();
        }
    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {
        dismissDialogLoading();
        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
                mContext, cacheSize)), Toast.LENGTH_LONG).show();
        header.setVisibility(View.GONE);
        bottom_lin.setVisibility(View.GONE);
        mCacheListItem.clear();
        rublishMemoryAdapter.notifyDataSetChanged();
    }

    private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
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

    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);

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
}

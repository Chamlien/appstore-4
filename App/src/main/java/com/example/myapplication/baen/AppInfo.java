package com.example.myapplication.baen;

import android.graphics.drawable.Drawable;

/**
 * 应用程序实体类说明
 * by:xuge
 */

public class AppInfo {
	private Drawable appIcon;//图标
	private String appName;//名称
	private String packname;//包名
	private String version;//版本号
    private long pkgSize;//大小
	private int uid;//id
	
	
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

    public long getPkgSize() {
        return pkgSize;
    }

    public void setPkgSize(long pkgSize) {
        this.pkgSize = pkgSize;
    }

    /**
	 * 应用程序可以被安装到不同的位置 , 手机内存 外部存储sd卡
	 */


	private boolean inRom;
	
	private boolean userApp;

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	
	
	
}

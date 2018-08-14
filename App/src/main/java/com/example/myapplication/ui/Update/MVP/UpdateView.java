package com.example.myapplication.ui.Update.MVP;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;

import java.util.List;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/6 15:58
 * History:
 * desc:
 **/
public interface UpdateView {

   void returnVersion(VersionCode versionCodes);
   void returnAppMessage(List<Apps.message> messages);

}

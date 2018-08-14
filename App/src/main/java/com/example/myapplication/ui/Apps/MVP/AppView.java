package com.example.myapplication.ui.Apps.MVP;

import com.example.myapplication.baen.Apps;

import java.util.List;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: AppView
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 14:40
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public interface AppView {
    void returnAppMessage(List<Apps.message> messages);
    void dismiss();
}

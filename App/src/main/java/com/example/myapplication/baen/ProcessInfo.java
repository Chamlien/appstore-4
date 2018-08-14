package com.example.myapplication.baen;

/**
 * 2 * Copyright (C), 2018-2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: 系统进程信息
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/6 13:59
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * desc:内存加速实体类
 */
public class ProcessInfo {
    /**
     * The user id of this process.
     */
    public String uid;

    /** The name of the process that this object is associated with. */
    public String processName;

    /** The pid of this process; 0 if none. */
    public int pid;

    /**  占用的内存 B. */
    public long memory;

    /**  占用的CPU. */
    public String cpu;

    /**  进程的状态，其中S表示休眠，R表示正在运行，Z表示僵死状态，N表示该进程优先值是负数. */
    public String status;

    /**  当前使用的线程数. */
    public String threadsCount;

    /**
     * Instantiates a new ab process info.
     */
    public ProcessInfo() {
        super();
    }

    /**
     * Instantiates a new ab process info.
     *
     * @param processName the process name
     * @param pid the pid
     */
    public ProcessInfo(String processName, int pid) {
        super();
        this.processName = processName;
        this.pid = pid;
    }


}


package com.example.myapplication.widget.textcounter.formatters;


import com.example.myapplication.widget.textcounter.Formatter;

/**
 * * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * * Author: dell 许格
 */
public class NoFormatter implements Formatter {

    @Override
    public String format(String prefix, String suffix, float value) {
        return prefix + value + suffix;
    }
}

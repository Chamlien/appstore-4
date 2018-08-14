package com.example.myapplication.widget.textcounter.formatters;


import com.example.myapplication.widget.textcounter.Formatter;

import java.text.DecimalFormat;

/**
 * * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * * Author: dell 许格
 */
public class DecimalFormatter implements Formatter {

    private final DecimalFormat format = new DecimalFormat("#.0");

    @Override
    public String format(String prefix, String suffix, float value) {
        return prefix + format.format(value) + suffix;
    }
}

package com.example.myapplication.widget.textcounter;

/**
 * * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * * Author: dell 许格
 */
public interface Formatter {

    /**
     * Format the value in anyway you want
     * Remember to prepend the prefix and append the suffix
     */
    String format(String prefix, String suffix, float value);

}

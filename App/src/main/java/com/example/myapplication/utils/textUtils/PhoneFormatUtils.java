package com.example.myapplication.utils.textUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/30 16:26
 * History:
 * desc:对EditText输入手机号进行格式化
 **/
public class PhoneFormatUtils implements TextWatcher {
    private EditText numberEditText;
    /**
     * 字符串编辑前长度
     */
    int beforeLen = 0;
    /**
     * 字符串编辑后长度
     */
    int afterLen = 0;


    public PhoneFormatUtils(EditText numberEditText) {
        this.numberEditText = numberEditText;
        numberEditText.addTextChangedListener(this);
        beforeString = numberEditText.getText().toString();
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeLen = s.length();
    }


    private boolean isChange = true;


    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method
        if (!isChange) {
            isChange = true;
            return;
        }
        String contents = numberEditText.getText().toString();
        afterLen = contents.length();
        if (afterLen == 0)
            return;
        contents = contents.replace(" ", "");
        if (beforeLen > afterLen) {
            contents = subStringFormat(contents);
            System.out.println(contents + "**");
        } else if ((beforeLen < afterLen)) {
            contents = addStringFormat(contents);
        }
        if (!isChange) {
            numberEditText.setText(contents);
            if (contents.length() >= 1)
                numberEditText.setSelection(contents.length());
        }
    }


    /**
     * 添加字符格式化
     *
     * @param contents
     * @return
     */
    private String addStringFormat(String contents) {
        // TODO Auto-generated method stub
        int length = contents.length();
        isChange = false;
        if (length == 1) {
            if (!contents.startsWith("1")) {
                contents = "";
                return contents;
            }
            return contents;
        }
        if (!contents.startsWith("1")) {
            contents = contents.substring(1);
            return contents;
        }
        if (!(contents.startsWith("13") || contents.startsWith("15") || contents.startsWith("17") || contents
                .startsWith("18"))) {
            if (length > 2) {
                contents = "1" + contents.substring(2);
            } else {
                contents = "1";
            }
            return contents;
        }
        if (length > 11) {
            contents = contents.substring(0, 11);
        }
        if (length > 3) {
            contents = contents.substring(0, 3) + " " + contents.substring(3);
        }
        if (length > 7) {
            contents = contents.substring(0, 8) + " " + contents.substring(8);
        }
        beforeString = contents;
        return contents;
    }


    /**
     * 记录删除字符之前字符串内容，如若删除后格式不符进行还原
     */
    private String beforeString = "";


    /**
     * 删除字符格式化
     *
     * @param contents
     * @return
     */
    private String subStringFormat(String contents) {
        // TODO Auto-generated method stub
        int length = contents.length();
        isChange = false;
        if (length == 0) {
            return contents;
        }
        if (length == 1) {
            if (contents.startsWith("1"))
                return contents;
            else
                return beforeString;
        }
        if (length >= 2) {
            if (!(contents.startsWith("13") || contents.startsWith("15") || contents
                    .startsWith("18"))) {
                return beforeString;
            }
        }
        if (length > 3) {
            contents = contents.substring(0, 3) + " " + contents.substring(3);
        }
        if (length > 7) {
            contents = contents.substring(0, 8) + " " + contents.substring(8);
        }
        beforeString = contents;
        return contents;
    }


}

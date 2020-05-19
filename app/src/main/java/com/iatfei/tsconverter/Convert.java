package com.iatfei.tsconverter;

import android.content.Context;

import com.zqc.opencc.android.lib.ChineseConverter;

import static com.zqc.opencc.android.lib.ConversionType.HK2S;

class Convert {

    static String openCCConv(String from, int type, Context context) {

        String result = ChineseConverter.convert(from, HK2S, context);
        return result;
    }

}

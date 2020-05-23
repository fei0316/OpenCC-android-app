package com.iatfei.tsconverter;

import android.content.Context;

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import static com.zqc.opencc.android.lib.ConversionType.HK2S;
import static com.zqc.opencc.android.lib.ConversionType.S2HK;
import static com.zqc.opencc.android.lib.ConversionType.S2T;
import static com.zqc.opencc.android.lib.ConversionType.S2TW;
import static com.zqc.opencc.android.lib.ConversionType.S2TWP;
import static com.zqc.opencc.android.lib.ConversionType.T2HK;
import static com.zqc.opencc.android.lib.ConversionType.T2S;
import static com.zqc.opencc.android.lib.ConversionType.T2TW;
import static com.zqc.opencc.android.lib.ConversionType.TW2S;
import static com.zqc.opencc.android.lib.ConversionType.TW2SP;

class Convert {

    static String openCCConv(String from, int type, Context context) {
        ConversionType convType = findType (type);
        return ChineseConverter.convert(from, convType, context);
    }

    private static ConversionType findType(int type){
        switch (type) {
            case 2:
                return S2TW;
            case 3:
                return S2HK;
            case 4:
                return S2TWP;
            case 5:
                return T2S;
            case 6:
                return TW2S;
            case 7:
                return HK2S;
            case 8:
                return TW2SP;
            case 9:
                return T2TW;
            case 10:
                return T2HK;
            default:
                return S2T;
        }

    }

    static int radioToType (boolean t1, boolean t2, boolean t3, boolean v1, boolean v2, boolean v3, boolean v4, boolean v5, boolean i1, boolean i2, boolean i3){
        if (i1 && v1 && t1)
            return 1;
        if (i1 && v2 && t1)
            return 2;
        if (i1 && v3 && t1)
            return 3;
        if (i2 && v2 && t1)
            return 4;
        if (i1 && v1 && t2)
            return 5;
        if (i1 && v4 && t2)
            return 6;
        if (i1 && v5 && t2)
            return 7;
        if (i3 && v4 && t2)
            return 8;
        if (i1 && v2 && t3)
            return 9;
        if (i1 && v3 && t3)
            return 10;
        return -1;
    }
}

/*
 * Copyright (c) 2020-2021 Fei Kuan.
 *
 * This file is part of Chinese Converter
 * (see <https://github.com/fei0316/OpenCC-android-app>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.iatfei.tsconverter;

import android.content.Context;

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import static com.zqc.opencc.android.lib.ConversionType.*;

class Convert {

    static String openCCConv(String from, int type, Context context) {
        ConversionType convType = findType (type);
        String converted = ChineseConverter.convert(from, convType, context);
        return converted;
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

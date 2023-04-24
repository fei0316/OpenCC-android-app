/*
 * Copyright (c) 2020-2023 Fei Kuan.
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

class ConvertUtils {

    static String openCCConv(String from, int type, Context context) {
        ConversionType convType = findType (type);
        if (convType == null) {
            return from;
        }
        return ChineseConverter.convert(from, convType, context);
    }

    private static ConversionType findType(int type){
        switch (type) {
            case Constant.S2T:
                return S2T;
            case Constant.S2TW:
                return S2TW;
            case Constant.S2HK:
                return S2HK;
            case Constant.S2TWP:
                return S2TWP;
            case Constant.T2S:
                return T2S;
            case Constant.TW2S:
                return TW2S;
            case Constant.HK2S:
                return HK2S;
            case Constant.TW2SP:
                return TW2SP;
            case Constant.T2TW:
                return T2TW;
            case Constant.T2HK:
                return T2HK;
            default:
                return null;
        }
    }

    static int radioToType (boolean t1, boolean t2, boolean t3, boolean v1, boolean v2, boolean v3, boolean v4, boolean v5, boolean i1, boolean i2, boolean i3){
        if (i1 && v1 && t1)
            return Constant.S2T;
        if (i1 && v2 && t1)
            return Constant.S2TW;
        if (i1 && v3 && t1)
            return Constant.S2HK;
        if (i2 && v2 && t1)
            return Constant.S2TWP;
        if (i1 && v1 && t2)
            return Constant.T2S;
        if (i1 && v4 && t2)
            return Constant.TW2S;
        if (i1 && v5 && t2)
            return Constant.HK2S;
        if (i3 && v4 && t2)
            return Constant.TW2SP;
        if (i1 && v2 && t3)
            return Constant.T2TW;
        if (i1 && v3 && t3)
            return Constant.T2HK;
        return -1;
    }
}

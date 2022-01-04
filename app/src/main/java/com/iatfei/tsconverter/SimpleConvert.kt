/*
 * Copyright (c) 2020-2022 Fei Kuan.
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

package com.iatfei.tsconverter

import android.content.Context
import java.io.ObjectInputStream

object SimpleConvert {
    private var charMap = HashMap<Int, ChineseTypes>();

    private fun loadMap(c: Context) {
        var objectInput = ObjectInputStream(c.getResources().openRawResource(R.raw.charmap));
        var readFile = objectInput.readObject()
        charMap = readFile as HashMap<Int, ChineseTypes>;
        objectInput.close();
    }

    @JvmStatic
    fun checkString(str: String, c: Context): ChineseTypes {
        if (charMap.size < 2) {
            loadMap(c)
        }
        var codePoint = 0xFFFF
        if (str != null) {
            for (i in str.indices) {
                if (i == str.length - 1 && !str[i].isHighSurrogate() && !str[i].isLowSurrogate()) {
                    codePoint = str[i].code
                } else {
                    if (!str[i].isHighSurrogate() && !str[i].isLowSurrogate() && !str[i+1].isHighSurrogate() && !str[i+1].isLowSurrogate()) { //is not surrogate pair
                        codePoint = str[i].code
                    } else if (str[i].isHighSurrogate() && Character.isSurrogatePair(str[i], str[i+1])){ //is surrogate pair, High first
                        codePoint = Character.toCodePoint(str[i], str[i+1])
                    } else if (str[i].isLowSurrogate() && Character.isSurrogatePair(str[i+1], str[i])) { //is surrogate pair, Low first
                        codePoint = Character.toCodePoint(str[i+1], str[i])
                    }
                }
                var tempResult = charMap.get(codePoint)
                if (tempResult != null) {
                    return tempResult
                }
            }
        }
        return ChineseTypes.NONE
    }
}

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

/*
* Special thanks to Renn on GitHub who contributed to this piece of code.
* Copyright (c) 2021 Renn. Released under GNU GPL v3+.
*/

package com.iatfei.tsconverter

import android.content.Context
import android.os.Build
import java.io.ObjectInputStream
import java.lang.StringBuilder

object SimpleConvert {
    private var charMap = HashMap<Int, ChineseTypes>()

    private fun loadMap(c: Context) {
        val objectInput = ObjectInputStream(c.resources.openRawResource(R.raw.charmap))
        val readFile = objectInput.readObject()
        charMap = readFile as HashMap<Int, ChineseTypes>
        objectInput.close()
    }

    @JvmStatic
    fun checkString(str: String, c: Context): ChineseTypes {
        // Idea: find the first unique Simplified/Traditional character (i.e. that character only exists in Traditional/Simplified)
        // Approach: The HashMap contains all Unicode code points of unique Trad/Simp characters and whether it's Trad or Simp,
        //              just search through the map.
        if (charMap.size < 2) {
            // if charMap is empty, load it
            loadMap(c)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val codePoints = StringBuilder(str).codePoints()
            for (codePoint in codePoints) {
                val tempResult = charMap[codePoint]
                if (tempResult != null) {
                    return tempResult
                }
            }
        } else {
            // old approach which does not support surrogates (beyond BMP)
            for (i in str.indices) {
                val tempCodePoint = Character.codePointAt(str, i)
                if (tempCodePoint < 0xD800 || tempCodePoint > 0xDFFF) {
                    val tempResult = charMap[tempCodePoint]
                    if (tempResult != null) {
                        return tempResult
                    }
                }
            }
        }
        return ChineseTypes.NONE
    }
}

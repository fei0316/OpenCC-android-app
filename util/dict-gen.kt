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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner
import java.net.URL
import java.util.HashMap

val TS_CHAR_URL = URL("https://raw.githubusercontent.com/BYVoid/OpenCC/master/data/dictionary/TSCharacters.txt")
val ST_CHAR_URL = URL("https://raw.githubusercontent.com/BYVoid/OpenCC/master/data/dictionary/STCharacters.txt")

var charMap = HashMap<Int, ChineseTypes>();

fun buildMap() {
    try {
        var scanner = Scanner(TS_CHAR_URL.openStream())
        while(scanner.hasNext()) {
            var chars = scanner.nextLine().trim().split('\t')
            if(!chars[1].contains(chars[0])) {		// check if this is an uniquely Traditional character
				System.out.println(chars + chars[0].codePointAt(0) + "trad")
				charMap.put(chars[0].codePointAt(0), ChineseTypes.TRADITIONAL_CHINESE);
            }
        }
    } catch (e: IOException) {
        println("Error saving TS_CHAR_URL")
    }

    try {
        var scanner = Scanner(ST_CHAR_URL.openStream())
        while(scanner.hasNext()) {
            var chars = scanner.nextLine().trim().split('\t')
            if(!chars[1].contains(chars[0])) {		// check if this is an uniquely Simplified character
				var codePoint = 0xFFFF
				codePoint = chars[0].codePointAt(0);
				if (codePoint != 0xFFFF) {
					if(charMap.get(codePoint) != null) {
						println("BOTH TRADITIONAL AND SIMPLIFIED???!!!!")
						charMap.remove(codePoint)
					} else {
						System.out.println(chars + chars[0].codePointAt(0) + "simp")
						charMap.put(codePoint, ChineseTypes.SIMPLIFIED_CHINESE)
					}
				}
            }
        }
    } catch (e: IOException) {
        println("Error saving ST_CHAR_URL")
    }
}

fun main(args: Array<String>) {
	buildMap()
	var myFileOutStream = FileOutputStream("/mnt/d/Downloads/kotlin-compiler-1.8.20/kotlinc/bin/OpenCC-SimpTradMap-20230423.bin");

	var myObjectOutStream = ObjectOutputStream(myFileOutStream);
	myObjectOutStream.writeObject(charMap)

	myObjectOutStream.close();
	myFileOutStream.close();
}
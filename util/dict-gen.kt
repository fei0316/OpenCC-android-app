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
            if(!chars[1].contains(chars[0])) {
				if (chars[0].length == 1) { //not surrogate pair
					charMap.put(chars[0][0].code, ChineseTypes.TRADITIONAL_CHINESE)
				} else if (chars[0].length == 2) {
					if (chars[0][0].isHighSurrogate() && Character.isSurrogatePair(chars[0][0], chars[0][1])){ //is surrogate pair, High first
						charMap.put(Character.toCodePoint(chars[0][0], chars[0][1]), ChineseTypes.TRADITIONAL_CHINESE)
					} else if (chars[0][0].isLowSurrogate() && Character.isSurrogatePair(chars[0][1], chars[0][0])) { //is surrogate pair, Low first
						charMap.put(Character.toCodePoint(chars[0][1], chars[0][0]), ChineseTypes.TRADITIONAL_CHINESE)
					}
				}
				
				
            }
        }
    } catch (e: IOException) {
        println("Error saving TS_CHAR_URL")
    }

    try {
        var scanner = Scanner(ST_CHAR_URL.openStream())
        while(scanner.hasNext()) {
            var chars = scanner.nextLine().trim().split('\t')
            if(!chars[1].contains(chars[0])) {
				var codePoint = 0xFFFF
				if (chars[0].length == 1) { //is not surrogate pair
					codePoint = chars[0][0].code
				} else if (chars[0].length == 2) {
					if (chars[0][0].isHighSurrogate() && Character.isSurrogatePair(chars[0][0], chars[0][1])){ //is surrogate pair, High first
						codePoint = Character.toCodePoint(chars[0][0], chars[0][1])
					} else if (chars[0][0].isLowSurrogate() && Character.isSurrogatePair(chars[0][1], chars[0][0])) { //is surrogate pair, Low first
						codePoint = Character.toCodePoint(chars[0][1], chars[0][0])
					}
				}
				if (codePoint != 0xFFFF) {
					if(charMap.get(codePoint) != null) {
						println("BOTH TRADITIONAL AND SIMPLIFIED???!!!!")
						charMap.remove(codePoint)
					}
					else {
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
	var myFileOutStream = FileOutputStream("/mnt/c/Users/Fei Kuan/Downloads/kotlin-compiler-1.6.10/kotlinc/bin/OpenCC-SimpTradMap-20220101.bin");

	var myObjectOutStream = ObjectOutputStream(myFileOutStream);
	myObjectOutStream.writeObject(charMap)

	myObjectOutStream.close();
	myFileOutStream.close();
}
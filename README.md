# Android OpenCC (Android 開放中文轉換)
An implementation of OpenCC for Android for the conversion between different variants of Chinese, with auto detection & conversion.

爲 Android 使用者開發的 OpenCC 中文轉換器。支援自動轉換。

My third project! Developed during the COVID-19 pandemic.

Special thanks to BYVoid for the [OpenCC project](https://github.com/BYVoid/OpenCC), and qichuan for the [Android OpenCC Library](https://github.com/qichuan/android-opencc).
Special thanks to Renn for code contribution.

My app can be downloaded in the following three sources. Thanks to [Reproducible Builds](https://f-droid.org/docs/Reproducible_Builds/), apk from all sources are signed with my own keys, allowing cross-updates.

<a href='https://play.google.com/store/apps/details?id=com.iatfei.tsconverter'>
   <img alt='Get it on Google Play'
        src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
        height=60/>
</a>

<a href="https://f-droid.org/app/com.iatfei.tsconverter">
    <img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
         alt="Get it on F-Droid" height="60">
</a>

GitHub Releases: <a href='https://github.com/fei0316/OpenCC-android-app/releases'>https://github.com/fei0316/OpenCC-android-app/releases</a>

## Features

- Automatically detect Traditional or Simplified Chinese to convert automatically (Easy Mode)
- 10 conversion modes (see below for details)
- Convert directly by selecting text in text field, selecting the option in menu, and replaces with converted text automatically.

## Conversion Modes

### Conversion Modes chart

| ID |        From        |          To           |                Chinese Variant (異體)                 |                Word Use (用詞)                 |
|:--:|:------------------:|:---------------------:|:---------------------------------------------------:|:--------------------------------------------:|
| 1  |     Simplified     |      Traditional      |             OpenCC standard (OpenCC 標準)             |              ❌ (no conversion)               |
| 2  |     Simplified     |      Traditional      |         Convert to Taiwan variant (臺灣正體標準)          |                      ❌                       |
| 3  |     Simplified     |      Traditional      |  Convert to Hong Kong variant (香港繁體標準/香港小學學習字詞表標準)  |                      ❌                       |
| 4  |     Simplified     |      Traditional      |         Convert to Taiwan variant (臺灣正體標準)          |      Convert to Taiwan phrases (臺灣常用詞彙)      |
| 5  |    Traditional     |      Simplified       |                          ❌                          |                      ❌                       |
| 6  |    Traditional     |      Simplified       |        Convert from Taiwan variant (臺灣正體標準)         |                      ❌                       |
| 7  |    Traditional     |      Simplified       | Convert from Hong Kong variant (香港繁體標準/香港小學學習字詞表標準) |                      ❌                       |
| 8  |    Traditional     |      Simplified       |        Convert from Taiwan variant (臺灣正體標準)         | Convert to Mainland China phrases (中國大陸常用詞彙) |
| 9  | OpenCC Traditional |  Taiwan Traditional   |         Convert to Taiwan variant (臺灣正體標準)          |                      ❌                       |
| 10 | OpenCC Traditional | Hong Kong Traditional |  Convert to Hong Kong variant (香港繁體標準/香港小學學習字詞表標準)  |                      ❌                       |


### Notes on OpenCC standard

**Note:** "no conversion" for Chinese variants converts inputs into the OpenCC standard, and no particular governmental standards are followed. It is based the contributors' research, and aims to avoid situations where one character in a variant can be multiple characters in another variant by splitting up as much as possible. 

For example, the character 「臺」 is used for 「臺灣」 while 「台」 is used for 「天台」 in Taiwan standard, but in Hong Kong standard 「台」 is used for both. In this case, the OpenCC standard adopts the Taiwanese convention when translating from Simplified Chinese where 「台」 is always used for both.

**注意：**「OpenCC 標準」的異體選項代表使用 OpenCC 標準而不依照任何政府標準。OpenCC 標準由貢獻者參照各領域的漢字異體用法得出，依照「能分則不合」的原則，避免一對多的問題。

以「台/臺」舉例：

| 標準           | Taiwan   | Rooftop  |
| -------------- | -------- | -------- |
| 臺灣標準       | 臺灣     | 天台     |
| 香港標準       | 台灣     | 天台     |
| **OpenCC標準** | **臺灣** | **天台** |

### Example

#### Simplified to Traditional

##### Original

```
这个用户应该使用鼠标点击这里来查看东涌的涌浪的图片
```

##### OpenCC Traditional

```
這個用戶應該使用鼠標點擊這裏來查看東涌的涌浪的圖片
```

##### Taiwan Traditional

```
這個用戶應該使用鼠標點擊這裡來查看東湧的湧浪的圖片
```

##### Hong Kong Traditional

```
這個用户應該使用鼠標點擊這裏來查看東涌的湧浪的圖片
```

##### Taiwan Traditional with Taiwan phrases

```
這個使用者應該使用滑鼠點選這裡來檢視東湧的湧浪的圖片
```

**Note:** 「涌/湧」 is an exception to the OpenCC's aim to separating as much as possible, as the contributors based their research on old literary and dictionaries and ignored the special use case in Cantonese-speaking region.

**註：**「涌/湧」是 OpenCC 「能分則不合」的例外，請見 https://zhuanlan.zhihu.com/p/104314323

#### Traditional to Simplified

##### Correct Simplified Chinese Result

```
我同学手中拿着一本有关自行车的名著
```

| Original Traditional Chinese                           | From OpenCC standard                               | From Taiwanese                             | From HK                                            | From Taiwanese (CN phrase)                 |
| ------------------------------------------------------ | -------------------------------------------------- | ------------------------------------------ | -------------------------------------------------- | ------------------------------------------ |
| 我同學手中拿**着**一本有關腳踏車的名**著**  (HK style) | 我同学手中拿**着**一本有关脚踏车的名**著**         | 我同学手中拿**着**一本有关脚踏车的名**著** | 我同学手中拿**着**一本有关脚踏车的名**著**         | 我同学手中拿**着**一本有关自行车的名**著** |
| 我同學手中拿**著**一本有關腳踏車的名**著** (TW style)  | 我同学手中拿**著**一本有关脚踏车的名**著** (wrong) | 我同学手中拿**着**一本有关脚踏车的名**著** | 我同学手中拿**著**一本有关脚踏车的名**著** (wrong) | 我同学手中拿**着**一本有关自行车的名**著** |

Therefore, please always choose the origin text to the best of your knowledge and double check the result. Most discrepancies are small  however, as demonstrated.

#### Traditional Variant Conversion

##### Original (OpenCC standard)

```
我拿着有關臺灣的名著
```

##### Hong Kong Standard

```
我拿着有關台灣的名著
```

##### Taiwan Standard

```
我拿著有關臺灣的名著
```

## Todo

- Wait for qichuan to update the library for the new Hong Kong variant updates no longer based on 香港小學學習字詞表 (OpenCC pull request #418)
- Japanese New Kanji support?

## How to build
Just clone the repository and open it in Android Studio. It *should* work...
### Notes on Easy Mode autodetection
The charmap.bin in res/raw is generated with code in util. It takes in data from OpenCC's GitHub page and compile a Serialized HashMap. Use Kotlin compiler to compile these two files and then run it to get the bin file. Change package name as needed.
This part of the code is not as clean as I wanted it to be, so pull requests are welcome!
```
./kotlinc ../dict-gen.kt ./ChineseTypes.class -include-runtime -d dict-gen.jar
java -jar ./dict-gen.jar
```

## Any issues?

For problems related to conversion database, please open an issue in OpenCC's [GitHub page](https://github.com/BYVoid/OpenCC).

Otherwise, please open an issue here.

This app's version numbers follows Romantic, [Sentimental Versioning](https://github.com/dominictarr/sentimental-versioning).
In the version number `x.y.z`, `x` will be updated when significant changes are made, `y` be updated when small changes are made, and `z` is reserved for minor bugfixes. 
So, don't ask about SemVer, it doesn't really make sense for an app like this anyway.

## Licenses
```
Copyright (c) 2020-2025 Fei Kuan.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

```

# Android OpenCC (Android 開放中文轉換)
An implementation of OpenCC for Android for the conversion between different variants of Chinese.

爲 Android 使用者開發的 OpenCC 中文轉換器。

My third project! Developed during the COVID-19 pandemic.

Special thanks to BYVoid for the [OpenCC project](https://github.com/BYVoid/OpenCC), and qichuan for the [Android OpenCC Library](https://github.com/qichuan/android-opencc).

## Features

- 10 conversion modes (see below for details)
- Convert directly by selecting text in text field, selecting the option in menu, and replaces with converted text automatically.

## Conversion Modes

### Notes on OpenCC standard

**Note:** "no conversion" for Chinese variants converts inputs into the OpenCC standard, and no particular governmental standards are followed. It is based the contributors' research, and aims to avoid situations where one character in a variant can be multiple characters in another variant by splitting up as much as possible. 

For example, the character 「臺」 is used for 「臺灣」 while 「台」 is used for 「天台」 in Taiwan standard, but in Hong Kong standard 「台」 is used for both. In this case, the OpenCC standard adopts the Taiwanese convention when translating from Simplified Chinese where 「台」 is always used for both.

**注意：**「不轉換」異體的選項代表使用 OpenCC 標準而不依照任何政府標準。OpenCC 標準由貢獻者參照各領域的漢字異體用法得出，依照「能分則不合」的原則，避免一對多的問題。

以「台/臺」舉例：

| 標準           | Taiwan   | Rooftop  |
| -------------- | -------- | -------- |
| 臺灣標準       | 臺灣     | 天台     |
| 香港標準       | 台灣     | 天台     |
| **OpenCC標準** | **臺灣** | **天台** |

### Conversion Modes chart

|  ID   |    From     |            To             |                    Chinese Variant (異體)                    |                   Word Use (用詞)                   |
| :--: | :---------: | :-----------------------: | :----------------------------------------------------------: | :-------------------------------------------------: |
|  1   | Simplified  |        Traditional        |                      ❌ (no conversion)                       |                  ❌ (no conversion)                  |
|  2   | Simplified  |        Traditional        |           Convert to Taiwan variant (臺灣正體標準)           |                          ❌                          |
|  3   | Simplified  |        Traditional        | Convert to Hong Kong variant (香港繁體標準/香港小學學習字詞表標準) |                          ❌                          |
|  4   | Simplified  |        Traditional        |           Convert to Taiwan variant (臺灣正體標準)           |       Conver to Taiwan phrases (臺灣常用詞彙)       |
|  5   | Traditional |        Simplified         |                              ❌                               |                          ❌                          |
|  6   | Traditional |        Simplified         |          Convert from Taiwan variant (臺灣正體標準)          |                          ❌                          |
|  7   | Traditional |        Simplified         | Convert from Hong Kong variant (香港繁體標準/香港小學學習字詞表標準) |                          ❌                          |
|  8   | Traditional |        Simplified         |          Convert from Taiwan variant (臺灣正體標準)          | Conver to Mainland China phrases (中國大陸常用詞彙) |
|  9   | Traditional | (variant conversion only) |           Convert to Taiwan variant (臺灣正體標準)           |                          ❌                          |
|  10  | Traditional | (variant conversion only) | Convert to Hong Kong variant (香港繁體標準/香港小學學習字詞表標準) |                          ❌                          |

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

**Note:** 「涌/湧」 is an exception to the OpenCC's aim to separating as much as possible, as the contributors based their research on old literary and dictionaries and ignored the special usecases in Cantonese-speaking region.

**註：**「涌/湧」是 OpenCC 「能分則不合」的例外，請見 https://zhuanlan.zhihu.com/p/104314323。

#### Traditional to Simplified

##### Correct Simplified Chinese Result

```
我同学手中拿着一本有关自行车的名著
```

| Original Traditional Chinese                           | From OpenCC standard                               | From Taiwanese                             | From HK                                            | From Taiwanese (CN phrase)                 |
| ------------------------------------------------------ | -------------------------------------------------- | ------------------------------------------ | -------------------------------------------------- | ------------------------------------------ |
| 我同學手中拿**着**一本有關腳踏車的名**著** （HK style) | 我同学手中拿**着**一本有关脚踏车的名**著**         | 我同学手中拿**着**一本有关脚踏车的名**著** | 我同学手中拿**着**一本有关脚踏车的名**著**         | 我同学手中拿**着**一本有关自行车的名**著** |
| 我同學手中拿**著**一本有關腳踏車的名**著** (TW style)  | 我同学手中拿**著**一本有关脚踏车的名**著** (wrong) | 我同学手中拿**着**一本有关脚踏车的名**著** | 我同学手中拿**著**一本有关脚踏车的名**著** (wrong) | 我同学手中拿**着**一本有关自行车的名**著** |

Therefore, please always choose the origin text to the best of your knowledge and double check the result. Most discrepensies are small  however, as demonstrated.

## Todo

- First-use tutorial (especially for the selection feature)
- Dark Mode
- Wait for qichuan to update the library for the new Hong Kong variant updates no longer based on 香港小學學習字詞表 (OpenCC pull request #418)
- Prompt that OpenCC is not perfect and should double check the result
- Japanese New Knaji support??
- Default conversion mode?
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

package com.iatfei.tsconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import androidx.core.content.edit

class FirstStartupActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isColorTransitionsEnabled = true
        setImmersiveMode()

        // todo: make someone design some graphics for this
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_1),
                description = getString(R.string.appintro_content_1),
                imageDrawable = R.drawable.ic_applogo_intro,
                backgroundColor = getColor(R.color.appIntroOne)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_2),
                description = getString(R.string.appintro_content_2),
                imageDrawable = R.drawable.ic_baseline_info_24,
                backgroundColor = getColor(R.color.appIntroTwo)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_3_1),
                description = getString(R.string.appintro_content_3_1),
                imageDrawable = R.drawable.tutorial_textselection_1,
                backgroundColor = getColor(R.color.appIntroFour)
        ))
        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.appintro_title_3_2),
            description = getString(R.string.appintro_content_3_2),
            imageDrawable = R.drawable.tutorial_textselection_2,
            backgroundColor = getColor(R.color.appIntroFour)
        ))
        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.appintro_title_3_3),
            description = getString(R.string.appintro_content_3_3),
            imageDrawable = R.drawable.tutorial_textselection_3,
            backgroundColor = getColor(R.color.appIntroFour)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_4),
                description = getString(R.string.appintro_content_4),
                imageDrawable = R.drawable.ic_applogo_intro,
                backgroundColor = getColor(R.color.appIntroThree)
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        prefs.edit {
            putInt(Constant.PREF_PREVIOUS_STARTED_VERSION, BuildConfig.VERSION_CODE)
        }
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        prefs.edit {
            putInt(Constant.PREF_PREVIOUS_STARTED_VERSION, BuildConfig.VERSION_CODE)
        }
        finish()
    }
}
package com.iatfei.tsconverter

import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class FirstStartupActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isColorTransitionsEnabled = true
        isWizardMode = true

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_1),
                description = getString(R.string.appintro_content_1),
                imageDrawable = R.drawable.ic_launcher_foreground,
                backgroundColor = getColor(R.color.appIntroOne)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_2),
                description = getString(R.string.appintro_content_2),
                imageDrawable = R.drawable.ic_launcher_foreground,
                backgroundColor = getColor(R.color.appIntroTwo)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_3),
                description = getString(R.string.appintro_content_3),
                imageDrawable = R.drawable.tutorial_textselection_1,
                backgroundColor = getColor(R.color.appIntroFour)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.appintro_title_4),
                description = getString(R.string.appintro_content_4),
                imageDrawable = R.drawable.ic_launcher_foreground,
                backgroundColor = getColor(R.color.appIntroThree)
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        val edit: Editor = prefs.edit()
        edit.putBoolean("previous_started", true)
        edit.apply()
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        val edit: Editor = prefs.edit()
        edit.putBoolean("previous_started", true)
        edit.apply()
        finish()
    }
}
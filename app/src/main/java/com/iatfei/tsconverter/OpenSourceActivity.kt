package com.iatfei.tsconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.LibsBuilder

class OpenSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = LibsBuilder()
                .withFields(R.string::class.java.fields) // in some cases it may be needed to provide the R class, if it can not be automatically resolved
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withLicenseShown(true)
                .withAboutAppName(getString(R.string.app_name))
                .withAboutDescription("I can't believe you clicked on this! Thank you for using my app!<br>Special thanks to BYVoid for developing OpenCC.<br><br>和平總是建基在理解之上。")
                .supportFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

    }
}

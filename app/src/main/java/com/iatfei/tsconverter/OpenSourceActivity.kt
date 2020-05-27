package com.iatfei.tsconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder

class OpenSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)

        val fragment = LibsBuilder()
                .withFields(R.string::class.java.fields) // in some cases it may be needed to provide the R class, if it can not be automatically resolved
                .withLibraryModification("aboutlibraries", Libs.LibraryFields.LIBRARY_NAME, "_AboutLibraries") // optionally apply modifications for library information
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription("This is a small sample which can be set in the about my app description file.<br /><b>You can style this with html markup :D</b>")
                .supportFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

    }
}

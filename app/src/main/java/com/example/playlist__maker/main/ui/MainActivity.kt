package com.example.playlist__maker.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist__maker.R
import com.example.playlist__maker.R.id.media_btn
import com.example.playlist__maker.R.id.mainBtnSearch
import com.example.playlist__maker.R.id.settings_btn
import com.example.playlist__maker.media.ui.MediaActivity
import com.example.playlist__maker.search.ui.SearchActivity
import com.example.playlist__maker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(mainBtnSearch)
        val buttonMedia = findViewById<Button>(media_btn)
        val buttonSettings = findViewById<Button>(settings_btn)

        buttonSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }


    }
}
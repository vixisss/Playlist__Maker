package com.example.playlist__maker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist__maker.R

class MediaActivity : AppCompatActivity() {
    private lateinit var rollback: Toolbar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        rollback = findViewById(R.id.media_toolbar)

        setupToolbar()
    }

    private fun  setupToolbar(){
        rollback.setOnClickListener {
            finish()
        }
    }
}
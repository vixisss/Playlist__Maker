package com.example.playlist__maker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist__maker.R.id.rollback

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rollbackBtn = findViewById<Button>(rollback)

        rollbackBtn.setOnClickListener {
            finish()
        }
    }
}
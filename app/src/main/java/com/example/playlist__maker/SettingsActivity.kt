package com.example.playlist__maker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //"назад"
        val rollback = findViewById<Toolbar>(R.id.settings_toolbar)
        rollback.setNavigationOnClickListener{
            finish()
        }

        // "Поделиться приложением"
        val shareButton = findViewById<Button>(R.id.btn_shareApp)
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settingsShare_Text))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        //"Написать в поддержку"
        val supportHelpButton = findViewById<Button>(R.id.btn_supportHelp)
        supportHelpButton.setOnClickListener{
            val message = getString(R.string.supportHelpBodyLetter_text)
            val theme = getString(R.string.supportHelpTheme_text)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myEmail)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, theme)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        //"Пользовательское соглашение"
        val userAgreeButton = findViewById<Button>(R.id.btn_userAgree)
        userAgreeButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.offerYandex))
            }
            startActivity(intent)
        }

    }
}

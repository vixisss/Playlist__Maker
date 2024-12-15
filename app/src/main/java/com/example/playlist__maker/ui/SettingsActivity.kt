package com.example.playlist__maker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.playlist__maker.presentation.App
import com.example.playlist__maker.R
import com.google.android.material.switchmaterial.SwitchMaterial

private const val EXAMPLE_PREFERENCES = "shared_preferences"
private const val SWITCH_THEME_KEY = "key_of_switch_theme"

class SettingsActivity : AppCompatActivity() {
    private lateinit var rollback: Toolbar
    private lateinit var shareButton: Button
    private lateinit var supportHelpButton: Button
    private lateinit var userAgreeButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        rollback = findViewById(R.id.settings_toolbar)
        shareButton = findViewById(R.id.btn_shareApp)
        supportHelpButton = findViewById(R.id.btn_supportHelp)
        userAgreeButton = findViewById(R.id.btn_userAgree)


        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val themeSharedPreferences = getSharedPreferences(EXAMPLE_PREFERENCES, MODE_PRIVATE)
        themeSwitcher.isChecked = (application as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            themeSharedPreferences.edit{
                putBoolean(SWITCH_THEME_KEY, checked)
                apply()
            }
        }

        rollback()
        shareApp()
        supportHelp()
        userAgree()
    }

    private fun rollback(){
        rollback.setNavigationOnClickListener{
            finish()
        }
    }

    private fun shareApp(){
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settingsShare_Text))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun supportHelp(){
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
    }

    private fun userAgree(){
        userAgreeButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.offerYandex))
            }
            startActivity(intent)
        }
    }
}

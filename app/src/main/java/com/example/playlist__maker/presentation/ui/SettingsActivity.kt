package com.example.playlist__maker.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.playlist__maker.presentation.utils.App
import com.example.playlist__maker.R
import com.example.playlist__maker.data.network.AppSwitcher
import com.example.playlist__maker.domain.api.AppSwitcherInteractor
import com.example.playlist__maker.presentation.utils.Creator
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var rollback: Toolbar
    private lateinit var shareButton: Button
    private lateinit var supportHelpButton: Button
    private lateinit var userAgreeButton: Button
    private lateinit var themeSwitcher: SwitchMaterial

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        rollback = findViewById(R.id.settings_toolbar)
        shareButton = findViewById(R.id.btn_shareApp)
        supportHelpButton = findViewById(R.id.btn_supportHelp)
        userAgreeButton = findViewById(R.id.btn_userAgree)
        themeSwitcher = findViewById(R.id.themeSwitcher)


        val appSwitcherInteractor = Creator.provideAppSwitcherInteractor()

        themeSwitcher.isChecked = appSwitcherInteractor.getTheme()
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            appSwitcherInteractor.switchTheme(checked)
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

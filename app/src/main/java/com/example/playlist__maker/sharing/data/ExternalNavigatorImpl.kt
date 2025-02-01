package com.example.playlist__maker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlist__maker.R
import com.example.playlist__maker.sharing.domain.repository.ExternalNavigator
import com.example.playlist__maker.sharing.domain.models.UriForUserAgree
import com.example.playlist__maker.sharing.domain.models.EmailData
import com.example.playlist__maker.sharing.domain.models.UriForShareApp

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {

    private fun makeEmailDataList(): EmailData = EmailData(
        emailAdress = arrayOf(context.getString(R.string.myEmail)),
        supportHelpTheme = context.getString(R.string.supportHelpTheme_text),
        supportHelpBodyLetter = context.getString(R.string.supportHelpBodyLetter_text),
        shareIntentData = "mailto:"
    )

    private fun makeUriForUserAgree(): UriForUserAgree = UriForUserAgree(
        url = context.getString(R.string.offerYandex)
    )

    private fun makeUriForShareApp(): UriForShareApp = UriForShareApp(
        url = context.getString(R.string.settingsShare_Text),
        type = "text/plain"
    )




    override fun shareApp() {
        val shareLinkData = makeUriForShareApp()
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareLinkData.url)
            setType(shareLinkData.type)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    override fun userAgree() {
        val browserLinkData = makeUriForUserAgree()
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(browserLinkData.url)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun supportHelp() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val emailData = makeEmailDataList()
            data = Uri.parse(emailData.shareIntentData)
            putExtra(Intent.EXTRA_EMAIL, emailData.emailAdress)
            putExtra(Intent.EXTRA_SUBJECT, emailData.supportHelpTheme)
            putExtra(Intent.EXTRA_TEXT, emailData.supportHelpBodyLetter)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
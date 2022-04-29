package com.example.android.finalyearproject.customer.appoint.detailAppoint

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class LinkToGoogleMapContract : ActivityResultContract<String, Unit>() {

    override fun createIntent(context: Context, input: String?): Intent {
        var intent = Intent()
        val gmmPackage = "com.google.android.apps.maps"
        intent.setPackage(gmmPackage)
        if (intent.resolveActivity(context.packageManager) != null) {
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(input))
            intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        } else {
            val appEnabled = context.packageManager.getApplicationEnabledSetting(gmmPackage)
            if (appEnabled == COMPONENT_ENABLED_STATE_DISABLED ||
                appEnabled == COMPONENT_ENABLED_STATE_DISABLED_USER) {
                intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:" + gmmPackage)
                }
            } else {
                intent = Intent(Intent.ACTION_VIEW, ).apply {
                    data = Uri.parse(
                        "http://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                    setPackage("com.android.vending")
                }
            }
        }

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }
}
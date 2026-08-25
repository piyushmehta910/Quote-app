package com.example.quoteapp.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object GoogleFontProvider {

    private var provider: androidx.compose.ui.text.googlefonts.GoogleFont.Provider? = null
    private var isGmsAvailable = false

    fun init(context: Context) {
        val result = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)
        isGmsAvailable = result == ConnectionResult.SUCCESS

        if (isGmsAvailable) {
            provider = androidx.compose.ui.text.googlefonts.GoogleFont.Provider(
                providerAuthority = "com.google.android.gms.fonts",
                providerPackage = "com.google.android.gms",
                certificates = com.example.quoteapp.R.array.com_google_android_gms_fonts_certs
            )
        }
    }

    @Composable
    fun getFontFamily(googleFontName: String, weight: FontWeight = FontWeight.Normal): FontFamily {
        val currentProvider = provider
        if (!isGmsAvailable || currentProvider == null) {
            return FontFamily.Default
        }
        val googleFont = androidx.compose.ui.text.googlefonts.GoogleFont(
            name = googleFontName
        )
        return FontFamily(
            androidx.compose.ui.text.googlefonts.Font(
                googleFont = googleFont,
                fontProvider = currentProvider,
                weight = weight
            )
        )
    }

    fun isAvailable(): Boolean = isGmsAvailable
}

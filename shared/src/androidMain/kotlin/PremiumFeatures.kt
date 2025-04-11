package com.example

actual class PremiumFeatures  {
    actual fun isPremium(): Boolean {
        // Use Android SharedPreferences or a hardcoded flag
        val sharedPrefX = androidx.preference.Preference
        val sharedPref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            android.app.ApplicationProvider.getApplicationContext()
        )
        return sharedPref.getBoolean("is_premium", false)
    }
}
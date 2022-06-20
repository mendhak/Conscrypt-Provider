package com.mendhak.conscryptprovider

import android.util.Log
import org.conscrypt.Conscrypt
import java.security.Security

@Suppress("unused")
class ConscryptProvider {

    //This is how to do static methods
    companion object {
        @JvmStatic
        fun install() {
            Log.d("GPSLoggerConscrypt", "Installing provider...");
            Security.insertProviderAt(Conscrypt.newProvider(), 1);
            Log.d("GPSLoggerConscrypt", "Provider installed successfully.");
        }
    }

}
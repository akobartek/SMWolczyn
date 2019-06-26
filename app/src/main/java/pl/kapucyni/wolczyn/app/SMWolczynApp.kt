package pl.kapucyni.wolczyn.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class SMWolczynApp : Application() {

    init {
        instance = this@SMWolczynApp
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(applicationContext)
    }
}
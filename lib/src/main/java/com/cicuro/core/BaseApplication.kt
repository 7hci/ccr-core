package com.cicuro.core

import android.app.Application
import android.content.Context
import com.cicuro.core.logging.SentryTree
import timber.log.Timber

abstract class BaseApplication : Application() {
    abstract val isDebug: Boolean

    override fun onCreate() {
        super.onCreate()
        application = this

        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(SentryTree())
        }
    }

    companion object {
        var application: Application? = null
        fun getContext(): Context? = application?.applicationContext
    }
}

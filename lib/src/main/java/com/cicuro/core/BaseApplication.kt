package com.cicuro.core

import android.app.Application
import com.cicuro.core.logging.SentryTree
import com.cicuro.core.models.LoginResult
import timber.log.Timber

abstract class BaseApplication : Application(){
  abstract val isDebug: Boolean
  abstract val applicationId: String
  abstract val versionName: String

  private var sentryTree: SentryTree? = null

  override fun onCreate() {
    super.onCreate()
    application = this

    if (isDebug) {
      Timber.plant(Timber.DebugTree())
    } else {
      sentryTree = SentryTree(applicationId, versionName)
      Timber.plant()
    }
  }

  fun initializeSentry(
    sentryDsn: String?,
    deviceId: Int?,
    loginResult: LoginResult?
  ) = sentryTree?.initializeSentry(applicationContext, sentryDsn, deviceId, loginResult)

  companion object {
    var application: Application? = null
  }
}

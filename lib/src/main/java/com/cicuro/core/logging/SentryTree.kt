package com.cicuro.core.logging

import android.os.Build
import android.util.Log
import com.cicuro.core.models.LoginResult
import io.sentry.Sentry
import io.sentry.SentryClientFactory
import io.sentry.event.User
import timber.log.Timber

class SentryTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority != Log.ERROR || Sentry.getStoredClient() == null) {
            return
        }

        if (t != null) {
            Sentry.capture(t)
        } else {
            Sentry.capture(message)
        }
    }

    companion object {
        private const val SERIAL = "SERIAL"
        private const val DEVICE_ID = "SERIAL"
        private const val PACKAGE_NAME = "PACKAGE_NAME"

        fun initializeSentry(
            sentryDSN: String?,
            packageName: String,
            deviceId: Int?,
            loginResult: LoginResult?
        ) {
            val client = SentryClientFactory.sentryClient(sentryDSN)
            client.tags = hashMapOf<String, String>(
                PACKAGE_NAME to packageName,
                SERIAL to Build.SERIAL,
                DEVICE_ID to deviceId.toString()
            )
            if (loginResult != null) {
                val user = User(loginResult.id, loginResult.username, null, null)
                client.context.user = user
            }

            Sentry.setStoredClient(client)
        }
    }
}
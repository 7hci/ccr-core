package com.cicuro.core.logging

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import com.cicuro.core.models.LoginResult
import io.sentry.Sentry
import io.sentry.SentryClientFactory
import io.sentry.android.event.helper.AndroidEventBuilderHelper
import io.sentry.event.User
import timber.log.Timber

private const val SERIAL = "SERIAL"
private const val DEVICE_ID = "SERIAL"
private const val PACKAGE_NAME = "PACKAGE_NAME"
private const val VERSION_NAME = "VERSION_NAME"

class SentryTree(
    val applicationId: String,
    val versionName: String
) : Timber.Tree() {
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

    @SuppressLint("HardwareIds")
    fun initializeSentry(
        context: Context,
        sentryDsm: String?,
        deviceId: Int?,
        loginResult: LoginResult?
    ) {
        val client = SentryClientFactory.sentryClient(sentryDsm)
        client.tags = hashMapOf<String, String>(
            PACKAGE_NAME to applicationId,
            VERSION_NAME to versionName,
            SERIAL to Build.SERIAL,
            DEVICE_ID to deviceId.toString()
        )
        if (loginResult != null) {
            val user = User(loginResult.id, loginResult.username, null, null)
            client.context.user = user
        }

        client.addBuilderHelper(AndroidEventBuilderHelper(context))

        Sentry.setStoredClient(client)
    }
}

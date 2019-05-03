package com.cicuro.core.models

import java.io.Serializable

data class Config(
    var version: String? = null,
    var sentryDSN: String? = null,
    var kioskModePassword: String? = null,
    var wallpaperUrl: String? = null,
    var pollingInterval: Long? = null,
    var alertThresholdMins: Int? = null,
    var sessionTimeout: Int? = null,
    var music: MusicServerConfig? = null,
    var proxy: ProxyServerConfig? = null,
    var apps: ArrayList<App>?,
    var deletedApps: ArrayList<App>?,
    var appCategories: ArrayList<AppCategory>?
) : Serializable {
    data class MusicServerConfig(
        var serverUri: String? = null,
        var username: String? = null,
        var password: String? = null
    ) : Serializable

    data class ProxyServerConfig(
        var host: String? = null,
        var port: Int? = null
    ) : Serializable
}

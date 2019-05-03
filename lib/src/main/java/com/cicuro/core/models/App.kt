package com.cicuro.core.models

data class App(
    var packageName: String? = null,
    var versionName: String? = null,
    var name: String? = null,
    var type: String? = null,
    var url: String? = null,
    var iconUrl: String? = null,
    var category: String? = null,
    var badge: ArrayList<Badge>? = null
) {
    data class Badge(
        var language: String? = null,
        var translation: String? = null
    )
}

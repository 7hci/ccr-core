package com.cicuro.core.models

data class AppCategory(
    var name: String? = null,
    var label: ArrayList<Label>? = null
) {
    data class Label(
        var language: String? = null,
        var translation: String? = null
    )
}

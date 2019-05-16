package com.cicuro.core.utils

import com.cicuro.core.BaseApplication
import com.i18next.android.I18Next

fun getI18Next(resourceMap: Map<String, Int>, language: String): I18Next {
    val i18n = I18Next()
    i18n.options.language = language
    i18n.options.defaultNamespace = "translation"
    i18n.options.keySeparator = "."
    i18n.options.nsSeparator = ":"
    i18n.options.interpolationPrefix = "{{"
    i18n.options.interpolationSuffix = "}}"

    for (entry in resourceMap.entries) {
        i18n.loader().namespace("translation").lang(entry.key).from(BaseApplication.getContext(), entry.value).load()
    }

    return i18n
}

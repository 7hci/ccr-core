package com.cicuro.core

import com.cicuro.core.models.Config
import com.cicuro.core.models.EnrollmentInfo
import com.cicuro.core.models.LoginResult
import com.cicuro.core.models.RequestHeaders
import com.cicuro.core.utils.getI18Next

abstract class BaseContext {
    abstract var config: Config?
    abstract var enrollmentInfo: EnrollmentInfo?
    abstract var requestHeaders: RequestHeaders?
    abstract var loginResult: LoginResult?
    abstract val translationsResourceMap: Map<String, Int>

    var i18n = getI18Next(mapOf(), "en")
      get() = getI18Next(translationsResourceMap, loginResult?.language ?: "en")
}

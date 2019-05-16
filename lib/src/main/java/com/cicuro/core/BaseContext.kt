package com.cicuro.core

import com.cicuro.core.models.Config
import com.cicuro.core.models.EnrollmentInfo
import com.cicuro.core.models.LoginResult
import com.cicuro.core.models.RequestHeaders

abstract class BaseContext {
    abstract var config: Config?
    abstract var enrollmentInfo: EnrollmentInfo?
    abstract var requestHeaders: RequestHeaders?
    abstract var loginResult: LoginResult?
}

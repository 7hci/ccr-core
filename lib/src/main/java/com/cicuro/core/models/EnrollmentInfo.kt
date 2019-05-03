package com.cicuro.core.models

import java.io.Serializable

data class EnrollmentInfo(
    var serverUrl: String? = null,
    var id: Int? = null
) : Serializable
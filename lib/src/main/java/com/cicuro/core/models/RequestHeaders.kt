package com.cicuro.core.models

import java.io.Serializable

data class RequestHeaders(
    var cookies: MutableList<String>? = null
) : Serializable

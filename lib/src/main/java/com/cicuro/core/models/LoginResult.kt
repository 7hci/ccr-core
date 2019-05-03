package com.cicuro.core.models

import org.joda.time.DateTime
import java.io.Serializable

data class LoginResult(
    var id: String? = null,
    var username: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var status: String? = null,
    var language: String? = null,
    var acceptedTerms: Boolean? = null,
    var createdAt: DateTime? = null,
    var updatedAt: DateTime? = null,
    var sessionDuration: Long? = null,
    var remainingTime: Long? = null,
    var apps: ArrayList<App>? = null,
    var loggedInAt: Long? = System.currentTimeMillis()
) : Serializable {
    fun fullName() = "$firstName $lastName"
}

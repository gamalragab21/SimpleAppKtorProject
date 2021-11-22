package com.example.helpers

import com.example.models.response.UserData
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(

    val message: String?,
    val states: Boolean = true,
    val token: String?=null
)
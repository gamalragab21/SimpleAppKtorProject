package com.example.models.requestsBody

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentialsLogin(
    val email:String,
    val password:String,
)
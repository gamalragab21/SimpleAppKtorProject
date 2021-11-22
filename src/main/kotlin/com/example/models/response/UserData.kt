package com.example.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id:Int,
    val username:String,
    val email:String,
    val image:String,
)
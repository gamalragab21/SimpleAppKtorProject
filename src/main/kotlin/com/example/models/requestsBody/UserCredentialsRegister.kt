package com.example.models.requestsBody

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentialsRegister(
    val username: String,
    val email: String,
    val image: String,
    val password: String
) {

    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

}
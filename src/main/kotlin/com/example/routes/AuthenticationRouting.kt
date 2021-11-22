package com.example.routes

import com.example.helpers.ConnectionDataBase
import com.example.helpers.AuthResponse
import com.example.models.requestsBody.UserCredentialsLogin
import com.example.models.requestsBody.UserCredentialsRegister
import com.example.models.response.UserData
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import entities.UserEntity
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Routing.authenticationRouting() {

    val db = ConnectionDataBase.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    route("/") {
        post("register") {
            val userCredentialsRegister = call.receive<UserCredentialsRegister>()

            val username = userCredentialsRegister.username
            val email = userCredentialsRegister.email
            val password = userCredentialsRegister.hashedPassword()
            val image = userCredentialsRegister.image

            // for check exists

            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.email eq email
                }.map {
                    it[UserEntity.email]
                }.firstOrNull()

            if (user != null) {
                call.respond(
                    HttpStatusCode.BadRequest, AuthResponse(
                        states = false,
                        message = "user already exists try to use another email",
                        token = null
                    )
                )
                return@post
            }
            val result=db.insert(UserEntity) {
                set(it.username, username)
                set(it.email, email)
                set(it.haspassord, password)
                set(it.image, image)
            }

            if (result==1) {
                val userInserted = db.from(UserEntity)
                    .select()
                    .where {
                        UserEntity.email eq email
                    }.map {
                        val id = it[UserEntity.userId] ?: -1
                        val email = it[UserEntity.email] ?: ""
                        val username = it[UserEntity.username] ?: ""
                        val image = it[UserEntity.image] ?: ""
                        UserData(id, username, email, image)
                    }.firstOrNull()
                val token=tokenManager.generateJWTToken(userInserted!!)
                call.respond(
                    HttpStatusCode.OK, AuthResponse(
                        states = true,
                        message = " Registration Successfully",
                        token = token
                    )
                )
                return@post
            }else{

                call.respond(
                    HttpStatusCode.BadRequest, AuthResponse(
                        states = false,
                        message = "Failed Registration",
                        token = null
                    )
                )
                return@post
            }


        }

        post("login") {
            val userCredentialsLogin = call.receive<UserCredentialsLogin>()

            val email = userCredentialsLogin.email
            val password = userCredentialsLogin.password

            var hasPassword: String? = null

            // for check exists

            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.email eq email
                }.where {
                    UserEntity.email eq email
                }.map {
                    val id = it[UserEntity.userId] ?: -1
                    val email = it[UserEntity.email] ?: ""
                    val username = it[UserEntity.username] ?: ""
                    val image = it[UserEntity.image] ?: ""
                    hasPassword = it[UserEntity.haspassord] ?: ""
                    UserData(id, username, email, image)
                }.firstOrNull()

            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest, AuthResponse(
                        states = false,
                        message = "Invalid email or password",
                        token = null
                    )
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, hasPassword ?: "")
            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.BadRequest, AuthResponse(
                        states = false,
                        message = "Invalid Password ",
                        token = null
                    )
                )
                return@post
            }
            val token=tokenManager.generateJWTToken(user)
            call.respond(
                HttpStatusCode.OK, AuthResponse(
                    states = true,
                    message = "user success logging.",
                    token = token
                )
            )

        }


    }


}
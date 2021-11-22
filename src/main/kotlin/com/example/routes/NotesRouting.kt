package com.example.routes

import com.example.helpers.ConnectionDataBase
import com.example.helpers.ConnectionDataBase.database
import com.example.helpers.NoteResponse
import com.example.models.requestsBody.NoteRequest
import com.example.models.response.NoteData
import com.example.models.response.UserData
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import entities.NoteEntity
import entities.UserEntity
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*

fun Route.notesRouting() {
    val db = ConnectionDataBase.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    route("/notes") {

        authenticate {

            get {
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val userId = principle!!.payload.getClaim("userId").asInt()
                if (userId < 0) {

                    call.respond(
                        HttpStatusCode.BadRequest,
                        NoteResponse(
                            states = false,
                            message = "Failed",
                            data = null
                        )
                    )
                } else {

                    var notes = database.from(NoteEntity).select()
                        .where {
                            NoteEntity.userId eq userId
                        }

                    val listNotes=ArrayList<NoteData>()
                    listNotes.clear()
                    for (row in notes){
                        listNotes.add(NoteData(
                            row[NoteEntity.id]?:-1,
                            row[NoteEntity.title]?:"",
                            row[NoteEntity.subTitle]?:"",
                            row[NoteEntity.dataTime]?:"",
                            row[NoteEntity.imagePath]?:"",
                            row[NoteEntity.note]?:"",
                            row[NoteEntity.color]?:"",
                            row[NoteEntity.webLink]?:"",
                            row[NoteEntity.userId]?:-1
                        ))
                    }
                    call.respond(HttpStatusCode.OK,
                        NoteResponse(
                            states = true,
                            message = "Success",
                            data = listNotes
                        ))
                }
            }

            post {
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val userId = principle!!.payload.getClaim("userId").asInt()
                val noteReceive=call.receive<NoteRequest>()
                if (userId < 0) {

                    call.respond(
                        HttpStatusCode.BadRequest,
                        NoteResponse(
                            states = false,
                            message = "Failed insert note ",
                            data = null
                        )
                    )
                    return@post
                } else {
                    if (noteReceive==null){
                        call.respond(
                            HttpStatusCode.BadRequest,
                            NoteResponse(
                                states = false,
                                message = "Note Body must require",
                                data = null
                            )
                        )
                        return@post
                    }
                    val result=db.insert(NoteEntity) {
                        set(it.title, noteReceive.title)
                        set(it.subTitle, noteReceive.subTitle)
                        set(it.note, noteReceive.note)
                        set(it.dataTime, noteReceive.dataTime)
                        set(it.color, noteReceive.color)
                        set(it.userId, userId)
                        set(it.imagePath, noteReceive.imagePath)
                        set(it.webLink, noteReceive.webLink)
                    }
//                    val noteInserted = db.from(UserEntity)
//                        .select()
//                        .where {
//                            NoteEntity.userId eq userId
//                        }.map {
//                            val id = it[NoteEntity.id] ?: -1
//                            val title = it[NoteEntity.title] ?: ""
//                            val subTitle = it[NoteEntity.subTitle] ?: ""
//                            val note = it[NoteEntity.note] ?: ""
//                            val imagePath = it[NoteEntity.imagePath] ?: ""
//                            val color = it[NoteEntity.color] ?: ""
//                            val timeData = it[NoteEntity.dataTime] ?: ""
//                            val webLink = it[NoteEntity.webLink] ?: ""
//                            val userId = it[NoteEntity.userId] ?: -1
//                            NoteData(id,title,subTitle,timeData,imagePath,note,color,webLink,userId)
//                        }.firstOrNull()

                    call.respond(
                        HttpStatusCode.OK,
                        NoteResponse(
                            states = true,
                            message = "successfully add note ",
                            data = null
                        )
                    )
                    return@post


                }
            }

        }
    }
}
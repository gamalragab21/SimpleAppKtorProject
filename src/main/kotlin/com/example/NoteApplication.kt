package com.example

import com.example.plugins.configureRouting
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import entities.Note
import entities.User
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select
import java.sql.DatabaseMetaData

fun main() {

    //todo first tes app
    embeddedServer(Netty, port = 4567, host = "0.0.0.0") {

//        configureRouting()
//        contactUsModel()
        install(ContentNegotiation) {
            json()
        }

        routing {
            notesRouting()


        }

    }.start(wait = true)



}


fun Route.notesRouting() {

    val notes = mutableListOf<Note>()

    route("/notes") {
        get {
            val id = call.request.queryParameters["id"]?:""
           if (id.isEmpty()) {
               if (notes.isNotEmpty()) {
                   call.respond(notes)
               } else {
                   call.respondText("No notes found", status = HttpStatusCode.NotFound)
               }
           }else{
               val note =
                   notes.find {
                       it.id == id.toInt()
                   } ?: return@get call.respondText(
                       "No notes with id $id",
                       status = HttpStatusCode.NotFound
                   )
               call.respond(note)

           }
        }

        get("{id}") {   // like BASE_URL/notes/5

            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val note =
                notes.find {
                    it.id == id.toInt()
                } ?: return@get call.respondText(
                    "No notes with id $id",
                    status = HttpStatusCode.NotFound
                )

            call.respond(note)

        }
        post {
            val note = call.receive<Note>()
            notes.add(note)
            call.respondText("Note stored correctly", status = HttpStatusCode.Created)

        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (notes.removeIf { it.id == id.toInt() }) {
                call.respondText("Note removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

}







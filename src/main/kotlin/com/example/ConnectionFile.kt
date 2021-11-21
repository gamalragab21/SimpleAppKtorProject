package com.example

import com.example.plugins.configureRouting
import entities.User
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main() {


// todo connection data base
    embeddedServer(Netty, port = 4567, host = "0.0.0.0") {


        install(ContentNegotiation) {
            json()
        }


        val database = Database.connect(
            url = "jdbc:mysql://localhost:3306/notes",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "Gamal@@2172001"
        )

        // to insert values to db
       // insertNote(database)



        // update note

       // updateNote(database)

        // delete note
        deleteNote(database)
        // read value from db
        readNote(database)

        configureRouting()

    }.start(wait = true)

}

fun deleteNote(database: Database) {

    database.delete(NotesEntity){
            it.id eq 5
    }

}

fun updateNote(database: Database) {
    database.update(NotesEntity) {
        set(it.note, "study Algo..")
        where {
            it.id eq 3
        }
    }
}

fun readNote(database: Database) {
    val notes = database.from(NotesEntity).select()
    notes.forEach {
        println("${it[NotesEntity.id]} , ${it[NotesEntity.title]} , ${it[NotesEntity.note]} , ${it[NotesEntity.timetstmp]}")
    }
}

fun insertNote(database: Database) {

    database.insert(NotesEntity) {
        set(it.title, "Gamal")
        set(it.note, "Study Soft Computing")
        set(it.timetstmp, "21/11/2021")
    }

}

fun Application.testFirstRouting() {

    routing {
        get("/") {

//            println("URL : ${call.request.uri}")  // '/'
//            println("HEADERS : ${call.request.headers.names()}")
//            println("HEADERS : ${call.request.headers["token"]}")
//            println("Query parmaters ${call.request.queryParameters.names()}")
//            println("Query parmaters NAME ${call.request.queryParameters["name"]}")
//            println("Query parmaters NAME ${call.request.queryParameters["email"]}")
            call.respondText("Gamal Started First Ktor App World!")
        }
        get("/notes/{note_id}") {
            val id = call.parameters["note_id"]
            call.respondText("Your note Id is $id")

        }

        post("/login") {
            val user = call.receive<User>()
            println("userInf is ${user.toString()}")
            call.respondText("EveryThing working")
        }

    }
}



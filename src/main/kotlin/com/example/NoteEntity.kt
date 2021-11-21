package com.example

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object NotesEntity:Table<Nothing>("Note"){


    val id=int("id").primaryKey()
    val title=varchar("title")
    val note=varchar("note")
    val timetstmp=varchar("timetstmp")


}
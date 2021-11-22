package com.example.models.response

import kotlinx.serialization.Serializable


@Serializable
data class NoteData (
    val id:Int,
    val title:String,
    val subTitle:String,
    val dataTime:String,
    val imagePath:String,
    val note:String,
    val color:String,
    val webLink:String,
    val userId:Int
        )

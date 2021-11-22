package com.example.models.requestsBody

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest (
    val title:String,
    val subTitle:String,
    val dataTime:String,
    val imagePath:String,
    val note:String,
    val color:String?=null,
    val webLink:String?=null
        )





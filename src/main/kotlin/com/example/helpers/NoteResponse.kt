package com.example.helpers

import com.example.models.response.NoteData
import com.example.models.response.UserData
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(

    val message: String?,
    val states: Boolean = true,
    val data: ArrayList<NoteData>?=null
)
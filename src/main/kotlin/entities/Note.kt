package entities

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id:Int,
    val title:String,
    val data:String,
    val message:String,
    val inFav:Boolean
)
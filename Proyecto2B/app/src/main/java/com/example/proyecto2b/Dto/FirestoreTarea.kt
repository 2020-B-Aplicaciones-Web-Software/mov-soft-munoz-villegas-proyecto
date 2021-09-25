package com.example.proyecto2b.Dto

import java.util.*

data class FirestoreTarea(
    val tituloTarea:String = "",
    val fechaEntrega:Date? = null,
    val intervaloRecordatorio:Int? = null,
    var uid:String = ""
)

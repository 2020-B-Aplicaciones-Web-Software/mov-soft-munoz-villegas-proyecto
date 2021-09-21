package com.example.proyecto2b.Dto

class FirestoreUsuarioDto(
    val uid:String="",
    val email:String="",
    var roles: ArrayList<String> = arrayListOf()

) {
}
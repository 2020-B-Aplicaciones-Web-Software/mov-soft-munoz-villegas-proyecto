package com.example.proyecto2b.Dto

import android.os.Parcel
import android.os.Parcelable

class FirestoreCategoriaDto (
    val uid:String?="",
    val nombre:String?=""
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(nombre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirestoreCategoriaDto> {
        override fun createFromParcel(parcel: Parcel): FirestoreCategoriaDto {
            return FirestoreCategoriaDto(parcel)
        }

        override fun newArray(size: Int): Array<FirestoreCategoriaDto?> {
            return arrayOfNulls(size)
        }
    }

}
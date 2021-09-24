package com.example.proyecto2b.Dto

import android.os.Parcel
import android.os.Parcelable

class FirestoreTarjetaDto(
    val uid:String?="",
    var titulo:String?="",
    var descripcion:String?=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirestoreTarjetaDto> {
        override fun createFromParcel(parcel: Parcel): FirestoreTarjetaDto {
            return FirestoreTarjetaDto(parcel)
        }

        override fun newArray(size: Int): Array<FirestoreTarjetaDto?> {
            return arrayOfNulls(size)
        }
    }

}
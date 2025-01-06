package com.example.latihan_lks

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: Int,
    val title: String,
    val image: String,
    val genre: String,
    val description: String,
    val releaseDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeInt(id)
        writeString(title)
        writeString(image)
        writeString(genre)
        writeString(description)
        writeString(releaseDate)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel) = Movie(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Movie?>(size)
    }
}

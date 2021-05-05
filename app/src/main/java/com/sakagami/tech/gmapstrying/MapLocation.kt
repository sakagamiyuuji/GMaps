package com.sakagami.tech.gmapstrying

import android.os.Parcel
import android.os.Parcelable

data class MapLocation(
    val markerName: String? = null,
    val langitude: Double? = null,
    val longitude: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(markerName)
        parcel.writeValue(langitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapLocation> {
        override fun createFromParcel(parcel: Parcel): MapLocation {
            return MapLocation(parcel)
        }

        override fun newArray(size: Int): Array<MapLocation?> {
            return arrayOfNulls(size)
        }
    }
}
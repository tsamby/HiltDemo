package com.wizzpass.hilt.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 20,September,2020
 */

@Entity(tableName = "resident")
data class Resident(
    @PrimaryKey(autoGenerate = true)
    val resId: Long = 0L,
    val carReg: String,
    val mobile: String,
    val address: String,
    var street_address: String,
    val fName: String,
    val lname: String,
    @Nullable
    val profImage: String,
    @Nullable
    val carImage: ArrayList<String>,
    @Nullable
    val additonalcarIds: ArrayList<String>,
    @Nullable
    val secondaryDriverIds: ArrayList<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("carImage"),
        TODO("additonalcarIds"),
        TODO("secondaryDriverIds")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(resId)
        parcel.writeString(carReg)
        parcel.writeString(mobile)
        parcel.writeString(address)
        parcel.writeString(street_address)
        parcel.writeString(fName)
        parcel.writeString(lname)
        parcel.writeString(profImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Resident> {
        override fun createFromParcel(parcel: Parcel): Resident {
            return Resident(parcel)
        }

        override fun newArray(size: Int): Array<Resident?> {
            return arrayOfNulls(size)
        }
    }
}























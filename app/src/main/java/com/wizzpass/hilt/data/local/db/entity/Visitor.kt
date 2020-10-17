package com.wizzpass.hilt.data.local.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 30,September,2020
 */

@Entity(tableName = "visitor")
data class Visitor(
    @PrimaryKey(autoGenerate = true)
    val visId: Long = 0L,
    val visCarReg: String,
    val visMobile: String,
    var resAddress: String,
    var res_street_address : String,
    val vis_fName: String,
    val vis_lname: String,
    @Nullable
    val vis_profImage: String,
    @Nullable
    var vis_carImage: ArrayList<String>,
    var reasonForVist : String,
    val visEntryTimeStamp: String,
    val visExitTimeStamp: String,
    val residentId: String
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
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(visId)
        parcel.writeString(visCarReg)
        parcel.writeString(visMobile)
        parcel.writeString(resAddress)
        parcel.writeString(res_street_address)
        parcel.writeString(vis_fName)
        parcel.writeString(vis_lname)
        parcel.writeString(vis_profImage)
        parcel.writeStringList(vis_carImage)
        parcel.writeString(reasonForVist)
        parcel.writeString(visEntryTimeStamp)
        parcel.writeString(visExitTimeStamp)
        parcel.writeString(residentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Visitor> {
        override fun createFromParcel(parcel: Parcel): Visitor {
            return Visitor(parcel)
        }

        override fun newArray(size: Int): Array<Visitor?> {
            return arrayOfNulls(size)
        }
    }
}


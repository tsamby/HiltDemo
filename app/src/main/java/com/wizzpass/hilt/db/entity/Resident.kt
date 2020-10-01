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
    val carImage: ArrayList<String>
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
        TODO("carImage")
    ) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Resident(resId=$resId, carReg='$carReg', mobile='$mobile', address='$address', street_address='$street_address', fName='$fName', lname='$lname', profImage='$profImage', carImage=$carImage)"
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















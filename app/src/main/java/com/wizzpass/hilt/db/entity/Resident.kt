package com.wizzpass.hilt.db.entity

import android.location.Address
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 20,September,2020
 */

@Entity(tableName = "resident")
data class Resident (
    @PrimaryKey(autoGenerate = true)
    val resId : Long = 0L,
    val fName : String,
    val lname : String,
    val mobile : String,
    val address: String,
    val carReg : String,
    @Nullable
    val profImage : String,
    @Nullable
    val carImage : String
)
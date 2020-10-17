package com.wizzpass.hilt.data.local.db.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 03,October,2020
 */
@Entity(tableName = "secondary_driver")
data class SecondaryDriver(
    @PrimaryKey(autoGenerate = true)
    val secondaryDriverId: Long = 0L,
    val carReg: String,
    val mobile: String,
    val address: String,
    val fName: String,
    val lname: String,

    @Nullable
    val profImage: String
)
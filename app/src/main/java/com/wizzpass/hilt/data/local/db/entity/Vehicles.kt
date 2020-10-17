package com.wizzpass.hilt.data.local.db.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 03,October,2020
 */

@Entity(tableName = "vehicles")
data class Vehicles (
    @PrimaryKey
    val carReg: String,
    val mobile: String,
    val address: String,
    @Nullable
    val profImage: String

)
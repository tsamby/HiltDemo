package com.wizzpass.hilt.db.entity

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
    val resCarReg: String,
    val resMobile: String,
    val resAddress: String,
    var visMobile: String,
    val visCarReg: String,
    val visEntryTimeStamp: String,
    val visExitTimeStamp: String
)
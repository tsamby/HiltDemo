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
)


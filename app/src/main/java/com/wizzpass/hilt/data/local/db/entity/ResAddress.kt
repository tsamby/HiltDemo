package com.wizzpass.hilt.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 23,September,2020
 */
@Entity(tableName = "res_address")
data class ResAddress (

    @PrimaryKey(autoGenerate = true)
    val resAdrressId : Long = 0L,

    @ColumnInfo(name = "addressNo")
    val resAddressNo : String,

    @ColumnInfo(name = "street_address")
    val resAddressStreet : String

)
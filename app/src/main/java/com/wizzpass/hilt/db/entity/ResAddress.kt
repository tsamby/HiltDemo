package com.wizzpass.hilt.db.entity

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

    @ColumnInfo(name = "address")
    val resAddress : String
)
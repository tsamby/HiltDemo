package com.wizzpass.hilt.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 20,September,2020
 */
@Entity(tableName = "supervisor")
data class Supervisor (
    @PrimaryKey(autoGenerate = true)
    val supId : Long = 0L,
    val userName : String,
    val password : String

)
package com.wizzpass.hilt.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by novuyo on 20,September,2020
 */
@Entity(tableName = "guard")
data class Guard (
    @PrimaryKey(autoGenerate = true)
    val guardId : Long = 0L,
    val userName : String,
    val password : String

)
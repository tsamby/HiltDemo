package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.ResAddress
import io.reactivex.Single
import java.util.ArrayList

/**
 * Created by novuyo on 23,September,2020
 */

@Dao
interface ResAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(resAddress: ResAddress) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAll(resAddress: ArrayList<ResAddress>) : List<Long>

    @Query("select * From res_address ORDER BY resAdrressId ASC")
    fun  fetch() : LiveData<MutableList<ResAddress>>

    @Query("DELETE FROM res_address")
    fun deleteAll()


    @Query("select * From res_address WHERE address = :address")
    fun  fetchAddress(address : String) : LiveData<ResAddress>


    @Insert
    fun insertUsingRx(resAddress: ResAddress): Single<Long>

    @Update
    fun update(resAddress: ResAddress): Single<Int>

    @Delete
    fun delete(resAddress: ResAddress) : Single<Int>

    @Insert
    fun insertMany(vararg resAddress: ResAddress) : Single<List<Long>>

    @Query("SELECT * FROM res_address")
    fun getAllUsers() : Single<List<ResAddress>>

    @Query("SELECT * FROM res_address WHERE resAdrressId = :id LIMIT 1")
    fun getResById(id : Long): Single<ResAddress>

    @Query("SELECT count(*) FROM res_address")
    fun count() : Single<Int>

}
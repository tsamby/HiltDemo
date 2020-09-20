package com.wizzpass.hilt.di

import android.content.Context
import com.wizzpass.hilt.db.ResidentDB
import com.wizzpass.hilt.db.dao.ResidentDao
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Created by novuyo on 20,September,2020
 */
@InstallIn(ApplicationComponent::class)
@Module
object DBModule {

    @Provides
    fun provideStudentDao(@ApplicationContext appContext: Context) : ResidentDao {
        return ResidentDB.getInstance(appContext).residentDao
    }

    @Provides
    fun provideStudentDBRepository(residentDao: ResidentDao) = ResidentDBRepository(residentDao)
}
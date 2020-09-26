package com.wizzpass.hilt.di

import android.content.Context

import com.wizzpass.hilt.db.ResidentDB
import com.wizzpass.hilt.db.dao.GuardDao
import com.wizzpass.hilt.db.dao.ResAddressDao
import com.wizzpass.hilt.db.dao.ResidentDao
import com.wizzpass.hilt.db.dao.SupervisorDao
import com.wizzpass.hilt.db.repository.GuardDBRepository
import com.wizzpass.hilt.db.repository.ResAddressDBRepository
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import com.wizzpass.hilt.db.repository.SupervisorDBRepository
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
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
    fun provideResidentDao(@ApplicationContext appContext: Context) : ResidentDao {
        return ResidentDB.getInstance(appContext).residentDao
    }

    @Provides
    fun provideResidentDBRepository(residentDao: ResidentDao) = ResidentDBRepository(residentDao)

    @Provides
    fun provideGuardDao(@ApplicationContext appContext: Context) : GuardDao {
        return ResidentDB.getInstance(appContext).guardDao
    }

    @Provides
    fun provideGuardDBRepository(guardDao: GuardDao) = GuardDBRepository(guardDao)

    @Provides
    fun provideResAddressDao(@ApplicationContext appContext: Context) : ResAddressDao {
        return ResidentDB.getInstance(appContext).resAddressDao
    }

    @Provides
    fun provideResAddressDBRepository(resAddressDao: ResAddressDao) = ResAddressDBRepository(resAddressDao)

    @Provides
    fun provideResSupervisorDao(@ApplicationContext appContext: Context) : SupervisorDao {
        return ResidentDB.getInstance(appContext).supervisorDao
    }

    @Provides
    fun provideResSupervisorDBRepository(supervisorDao: SupervisorDao) = SupervisorDBRepository(supervisorDao)

}
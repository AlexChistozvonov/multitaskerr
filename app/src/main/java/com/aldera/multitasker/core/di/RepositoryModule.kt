package com.aldera.multitasker.core.di

import com.aldera.multitasker.data.login.LoginRepositoryImpl
import com.aldera.multitasker.data.registration.RegistrationRepositoryImpl
import com.aldera.multitasker.domain.login.LoginRepository
import com.aldera.multitasker.domain.registration.RegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun loginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Singleton
    @Binds
    abstract fun registrationRepository(
        registrationRepository: RegistrationRepositoryImpl
    ): RegistrationRepository
}

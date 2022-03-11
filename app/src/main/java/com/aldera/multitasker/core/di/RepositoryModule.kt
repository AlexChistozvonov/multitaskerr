package com.aldera.multitasker.core.di

import com.aldera.multitasker.data.common.CommonRepositoryImpl
import com.aldera.multitasker.data.login.LoginRepositoryImpl
import com.aldera.multitasker.data.recovery.code.RecoveryPasswordCodeRepositoryImpl
import com.aldera.multitasker.data.recovery.create.RecoveryPasswordCreateRepositoryImpl
import com.aldera.multitasker.data.recovery.email.RecoveryPasswordEmailRepositoryImpl
import com.aldera.multitasker.data.registration.RegistrationRepositoryImpl
import com.aldera.multitasker.data.user.UserRepositoryImpl
import com.aldera.multitasker.domain.common.CommonRepository
import com.aldera.multitasker.domain.login.LoginRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCodeRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCreateRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordEmailRepository
import com.aldera.multitasker.domain.registration.RegistrationRepository
import com.aldera.multitasker.domain.user.UserRepository
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

    @Singleton
    @Binds
    abstract fun recoveryPasswordEmailRepository(
        recoveryPasswordEmailRepository: RecoveryPasswordEmailRepositoryImpl
    ): RecoveryPasswordEmailRepository

    @Singleton
    @Binds
    abstract fun recoveryPasswordCodeRepository(
        recoveryPasswordCodeRepository: RecoveryPasswordCodeRepositoryImpl
    ): RecoveryPasswordCodeRepository

    @Singleton
    @Binds
    abstract fun recoveryPasswordCreateRepository(
        recoveryPasswordCreateRepository: RecoveryPasswordCreateRepositoryImpl
    ): RecoveryPasswordCreateRepository

    @Singleton
    @Binds
    abstract fun userRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun commonRepository(
        commonRepository: CommonRepositoryImpl
    ): CommonRepository
}

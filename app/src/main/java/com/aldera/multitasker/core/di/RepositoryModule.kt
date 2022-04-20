package com.aldera.multitasker.core.di

import com.aldera.multitasker.data.appointed.TaskCountRepositoryImpl
import com.aldera.multitasker.data.appointed.UserSubtaskRepositoryImpl
import com.aldera.multitasker.data.appointed.UserTaskRepositoryImpl
import com.aldera.multitasker.data.category.create.CreateCategoryRepositoryImpl
import com.aldera.multitasker.data.category.delete.DeleteCategoryRepositoryImpl
import com.aldera.multitasker.data.category.list.ListCategoryRepositoryImpl
import com.aldera.multitasker.data.common.CommonRepositoryImpl
import com.aldera.multitasker.data.editPassword.EditPasswordRepositoryImpl
import com.aldera.multitasker.data.executor.ExecutorRepositoryImpl
import com.aldera.multitasker.data.exitProfile.ExitProfileRepositoryImpl
import com.aldera.multitasker.data.login.LoginRepositoryImpl
import com.aldera.multitasker.data.project.create.CreateProjectRepositoryImpl
import com.aldera.multitasker.data.project.delete.DeleteProjectRepositoryImpl
import com.aldera.multitasker.data.project.list.ProjectListRepositoryImpl
import com.aldera.multitasker.data.recovery.code.RecoveryPasswordCodeRepositoryImpl
import com.aldera.multitasker.data.recovery.create.RecoveryPasswordCreateRepositoryImpl
import com.aldera.multitasker.data.recovery.email.RecoveryPasswordEmailRepositoryImpl
import com.aldera.multitasker.data.registration.RegistrationRepositoryImpl
import com.aldera.multitasker.data.subtask.create.CreateSubtaskRepositoryImpl
import com.aldera.multitasker.data.subtask.delete.DeleteSubtaskRepositoryImpl
import com.aldera.multitasker.data.subtask.view.ViewSubtaskRepositoryImpl
import com.aldera.multitasker.data.task.create.CreateTaskRepositoryImpl
import com.aldera.multitasker.data.task.delete.DeleteTaskRepositoryImpl
import com.aldera.multitasker.data.task.list.TaskListRepositoryImpl
import com.aldera.multitasker.data.task.view.ViewTaskRepositoryImpl
import com.aldera.multitasker.data.user.UserRepositoryImpl
import com.aldera.multitasker.data.user.list.UserListRepositoryImpl
import com.aldera.multitasker.domain.appointed.TaskCountRepository
import com.aldera.multitasker.domain.appointed.UserSubtaskRepository
import com.aldera.multitasker.domain.appointed.UserTaskRepository
import com.aldera.multitasker.domain.category.create.CreateCategoryRepository
import com.aldera.multitasker.domain.category.delete.DeleteCategoryRepository
import com.aldera.multitasker.domain.category.list.ListCategoryRepository
import com.aldera.multitasker.domain.common.CommonRepository
import com.aldera.multitasker.domain.editPassword.EditPasswordRepository
import com.aldera.multitasker.domain.executor.ExecutorRepository
import com.aldera.multitasker.domain.exitProfile.ExitProfileRepository
import com.aldera.multitasker.domain.login.LoginRepository
import com.aldera.multitasker.domain.project.create.CreateProjectRepository
import com.aldera.multitasker.domain.project.delete.DeleteProjectRepository
import com.aldera.multitasker.domain.project.list.ProjectListRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCodeRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCreateRepository
import com.aldera.multitasker.domain.recovery.RecoveryPasswordEmailRepository
import com.aldera.multitasker.domain.registration.RegistrationRepository
import com.aldera.multitasker.domain.subtask.create.CreateSubtaskRepository
import com.aldera.multitasker.domain.subtask.delete.DeleteSubtaskRepository
import com.aldera.multitasker.domain.subtask.view.ViewSubtaskRepository
import com.aldera.multitasker.domain.task.create.CreateTaskRepository
import com.aldera.multitasker.domain.task.delete.DeleteTaskRepository
import com.aldera.multitasker.domain.task.list.TaskListRepository
import com.aldera.multitasker.domain.task.view.ViewTaskRepository
import com.aldera.multitasker.domain.user.UserRepository
import com.aldera.multitasker.domain.user.list.UserListRepository
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

    @Singleton
    @Binds
    abstract fun editPasswordRepository(
        editPasswordRepository: EditPasswordRepositoryImpl
    ): EditPasswordRepository

    @Singleton
    @Binds
    abstract fun exitProfile(
        exitProfileRepository: ExitProfileRepositoryImpl
    ): ExitProfileRepository

    @Singleton
    @Binds
    abstract fun getCategory(
        listCategoryRepository: ListCategoryRepositoryImpl
    ): ListCategoryRepository

    @Singleton
    @Binds
    abstract fun getProject(
        projectListRepository: ProjectListRepositoryImpl
    ): ProjectListRepository

    @Singleton
    @Binds
    abstract fun crateCategory(
        createCategoryRepository: CreateCategoryRepositoryImpl
    ): CreateCategoryRepository

    @Singleton
    @Binds
    abstract fun deleteCategory(
        deleteCategoryRepository: DeleteCategoryRepositoryImpl
    ): DeleteCategoryRepository

    @Singleton
    @Binds
    abstract fun createProject(
        createProjectRepository: CreateProjectRepositoryImpl
    ): CreateProjectRepository

    @Singleton
    @Binds
    abstract fun deleteProject(
        deleteProjectRepository: DeleteProjectRepositoryImpl
    ): DeleteProjectRepository

    @Singleton
    @Binds
    abstract fun createTask(
        createTaskRepository: CreateTaskRepositoryImpl
    ): CreateTaskRepository

    @Singleton
    @Binds
    abstract fun getTask(
        getTaskListRepository: TaskListRepositoryImpl
    ): TaskListRepository

    @Singleton
    @Binds
    abstract fun getViewTask(
        getViewTask: ViewTaskRepositoryImpl
    ): ViewTaskRepository

    @Singleton
    @Binds
    abstract fun deleteTask(
        deleteTask: DeleteTaskRepositoryImpl
    ): DeleteTaskRepository

    @Singleton
    @Binds
    abstract fun createSubtask(
        createSubtaskRepository: CreateSubtaskRepositoryImpl
    ): CreateSubtaskRepository

    @Singleton
    @Binds
    abstract fun getViewSubtask(
        viewSubtaskRepository: ViewSubtaskRepositoryImpl
    ): ViewSubtaskRepository

    @Singleton
    @Binds
    abstract fun deleteSubtask(
        deleteSubtask: DeleteSubtaskRepositoryImpl
    ): DeleteSubtaskRepository

    @Singleton
    @Binds
    abstract fun getUserTask(
        getUserTask: UserTaskRepositoryImpl
    ): UserTaskRepository

    @Singleton
    @Binds
    abstract fun getUserSubtask(
        getUserSubtask: UserSubtaskRepositoryImpl
    ): UserSubtaskRepository

    @Singleton
    @Binds
    abstract fun getTaskCount(
        getTaskCount: TaskCountRepositoryImpl
    ): TaskCountRepository

    @Singleton
    @Binds
    abstract fun getUserList(
        getUserList: UserListRepositoryImpl
    ): UserListRepository

    @Singleton
    @Binds
    abstract fun editTaskExecutor(
        editTaskExecutor: ExecutorRepositoryImpl
    ): ExecutorRepository
}

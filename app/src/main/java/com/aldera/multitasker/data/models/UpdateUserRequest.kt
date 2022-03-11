package com.aldera.multitasker.data.models

sealed class UpdateUserData {
    data class UpdateUser(val user: PutUserRequest) : UpdateUserData()
    data class UpdatePassword(val password: String) : UpdateUserData()
    data class UpdateImage(val user: PutUserRequest) : UpdateUserData()
    data class DeleteImage(val user: PutUserRequest) : UpdateUserData()
}

data class UpdateUserRequest(val payload: UpdateUserData)

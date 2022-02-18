package com.aldera.multitasker.presentation.recovery_password_email

sealed class RecoveryPasswordEmailEvent{
    object Loading: RecoveryPasswordEmailEvent()
    object EmailError: RecoveryPasswordEmailEvent()
}

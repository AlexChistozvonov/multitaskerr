package com.aldera.multitasker.presentation.recovery.email

sealed class RecoveryPasswordEmailEvent {
    object Loading : RecoveryPasswordEmailEvent()
    object EmailError : RecoveryPasswordEmailEvent()
}

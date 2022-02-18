package com.aldera.multitasker.presentation.recovery_password_email

import androidx.lifecycle.ViewModel
import com.aldera.multitasker.ConstantRegex
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoveryPasswordEmailViewModel @Inject constructor() : ViewModel() {


    private val regEmail = Regex("${ConstantRegex.REGEX_EMAIL}")

    fun sendForEmail() {


    }
}
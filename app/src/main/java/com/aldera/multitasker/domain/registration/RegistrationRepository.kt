package com.aldera.multitasker.domain.registration

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.RegistrationResponse

interface RegistrationRepository {
    suspend fun registration(
        login: String,
        password: String,
        password2: String,
    ): LoadingResult<RegistrationResponse>
}

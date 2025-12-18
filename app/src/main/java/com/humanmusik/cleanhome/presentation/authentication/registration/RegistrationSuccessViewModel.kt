package com.humanmusik.cleanhome.presentation.authentication.registration

import androidx.lifecycle.ViewModel

class RegistrationSuccessViewModel : ViewModel() {
    fun onOkPressed(navigation: () -> Unit) { navigation() }
}
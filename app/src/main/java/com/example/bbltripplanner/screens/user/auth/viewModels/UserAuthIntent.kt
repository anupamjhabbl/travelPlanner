package com.example.bbltripplanner.screens.user.auth.viewModels

import com.example.bbltripplanner.screens.user.auth.entity.OTPAction

class UserAuthIntent {
    class OTPAuth {
        sealed interface ViewEvent {
            data object ResendOTP : ViewEvent
            data object VerifyOTP : ViewEvent
            data class OnAction(val action: OTPAction) : ViewEvent
            data class SetData(val email: String, val userId: String): ViewEvent
        }

        sealed interface ViewEffect {}
    }

    class RegisterAuth {
        sealed interface ViewEvent {
            data class UpdateEmail(val email: String): ViewEvent
            data class UpdatePassword(val password: String): ViewEvent
            data class UpdateUsername(val username: String): ViewEvent
            data object RegisterUser: ViewEvent
        }

        sealed interface ViewEffect {}
    }

    class LoginAuth {
        sealed interface ViewEvent {
            data class UpdateEmail(val email: String): ViewEvent
            data class UpdatePassword(val password: String): ViewEvent
            data object LoginUser: ViewEvent
        }

        sealed interface ViewEffect {}
    }

    class ForgetPasswordAuth {
        sealed interface ViewEvent {
            data class UpdateEmail(val email: String): ViewEvent
            data object ResetPassword: ViewEvent
        }

        sealed interface ViewEffect {}
    }

    class ResetPasswordAuth {
        sealed interface ViewEvent {
            data class UpdatePassword(val password: String): ViewEvent
            data class UpdateConfirmPassword(val password: String): ViewEvent
            data object ResetPassword: ViewEvent
        }

        sealed interface ViewEffect {}
    }
}
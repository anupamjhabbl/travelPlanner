package com.example.bbltripplanner.main

import android.graphics.Color
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.bbltripplanner.R
import com.example.bbltripplanner.main.viewModels.MainActivityIntent
import com.example.bbltripplanner.main.viewModels.MainActivityViewModel
import com.example.bbltripplanner.navigation.AppNavigationComposable
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.ui.theme.TripPlannerTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {
    private val authPreferencesUseCase: AuthPreferencesUseCase by inject()
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        getViewModel<MainActivityViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val animation =
                    AnimationUtils.loadAnimation(this, R.anim.splas_plane_exit_animation)

                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        splashScreenView.remove()
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                })

                splashScreenView.iconView.startAnimation(animation)
            }
        }

        mainActivityViewModel.processEvent(MainActivityIntent.ViewEvent.Init)

        setContent {
            TripPlannerTheme {
                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppNavigationComposable(authPreferencesUseCase.getAccessToken())
                }
            }
        }
    }
}
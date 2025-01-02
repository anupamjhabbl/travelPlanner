package com.example.bbltripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.example.bbltripplanner.ui.theme.TripPlannerTheme
import com.example.bbltripplanner.user.profile.composables.MyProfileView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
        setContent {
            TripPlannerTheme {
                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalCustomColors.current.primaryBackground)
                ){
                    MyProfileView()
                }
            }
        }
    }
}
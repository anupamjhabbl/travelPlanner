package com.example.tripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.tripplanner.common.composables.ComposeTextView
import com.example.tripplanner.ui.theme.LocalCustomColors
import com.example.tripplanner.ui.theme.TripPlannerTheme
import com.example.tripplanner.user.profile.composables.MyProfileView

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
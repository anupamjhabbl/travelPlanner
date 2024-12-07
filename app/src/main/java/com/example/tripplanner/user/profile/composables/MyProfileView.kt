package com.example.tripplanner.user.profile.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyProfileView (

) {
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Hello world", modifier = Modifier.padding(it))
    }
}
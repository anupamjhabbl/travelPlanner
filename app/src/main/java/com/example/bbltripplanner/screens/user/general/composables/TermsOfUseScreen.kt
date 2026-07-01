package com.example.bbltripplanner.screens.user.general.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun TermsOfUseScreen() {
    val customColors = LocalCustomColors.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        TermsToolbar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.terms_last_updated),
                textColor = customColors.hintTextColor,
                fontSize = 13.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val termsSections = com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper.getTermsOfUseSections()
            termsSections.forEach { section ->
                SectionHeader(title = stringResource(id = section.titleRes))
                SectionBody(text = stringResource(id = section.descriptionRes))
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun TermsToolbar() {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                }
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.size(28.dp),
                contentDescription = "Back",
                tint = customColors.secondaryBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.padding(top = 9.dp)
        ) {
            ComposeTextView.TitleTextView(
                text = stringResource(R.string.terms_of_use_title),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    val customColors = LocalCustomColors.current
    ComposeTextView.TitleTextView(
        text = title,
        fontSize = 17.sp,
        textColor = customColors.titleTextColor
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SectionBody(text: String) {
    val customColors = LocalCustomColors.current
    ComposeTextView.TextView(
        text = text,
        fontSize = 15.sp,
        textColor = customColors.hintTextColor,
        lineHeight = 22.sp
    )
    Spacer(modifier = Modifier.height(24.dp))
}

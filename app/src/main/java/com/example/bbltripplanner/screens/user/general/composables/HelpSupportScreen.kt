package com.example.bbltripplanner.screens.user.general.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.general.viewModels.FAQCategory
import com.example.bbltripplanner.screens.user.general.viewModels.FAQItem
import com.example.bbltripplanner.screens.user.general.viewModels.HelpSupportViewModel
import com.example.bbltripplanner.screens.user.general.viewModels.HelpSupportViewModelIntent
import com.example.bbltripplanner.screens.user.general.viewModels.SmartSuggestion
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HelpSupportScreen() {
    val customColors = LocalCustomColors.current
    val viewModel: HelpSupportViewModel = koinViewModel()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val workUnderProgress = stringResource(R.string.work_under_progress)
    val scope = rememberCoroutineScope()
    val expandedCategories by viewModel.expandedCategories.collectAsStateWithLifecycle()
    var searchQuery by remember {
        mutableStateOf("")
    }
    val smartSuggestions = viewModel.getSmartSuggestions()
    val categories = viewModel.getFAQCategories(
        tripsIcon = Icons.Default.Map,
        expensesIcon = Icons.Default.Payments,
        mediaIcon = Icons.Default.PermMedia,
        socialIcon = Icons.Default.Person,
        notificationsIcon = Icons.Default.Notifications,
        securityIcon = Icons.Default.Security,
        issuesIcon = Icons.Default.BugReport,
        contactIcon = Icons.Default.SupportAgent
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(customColors.primaryBackground)
        ) {
            HelpSupportToolbar(
                {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.UserSettingsScreen.route
                            )
                        )
                    }
                }
            ) {
                scope.launch {
                    CommonNavigationChannel.navigateTo(
                        NavigationAction.NavigateUp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.how_can_we_assist),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = customColors.titleTextColor,
                        lineHeight = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.search_faqs),
                            color = customColors.hintTextColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = customColors.hintTextColor
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        unfocusedContainerColor = customColors.primaryBackground,
                        focusedContainerColor = customColors.primaryBackground,
                        unfocusedIndicatorColor = customColors.hintTextColor.copy(alpha = 0.2f),
                        focusedIndicatorColor = customColors.secondaryBackground
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.processEvent(HelpSupportViewModelIntent.ViewEvent.PerformSearch(categories, searchQuery))
                            focusManager.clearFocus()
                        }
                    )
                )

                Column {
                    Text(
                        text = stringResource(id = R.string.smart_suggestions),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = customColors.titleTextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SmartSuggestionsPager(smartSuggestions)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SmallSuggestionCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(id = R.string.expense_sync_issues),
                            icon = Icons.AutoMirrored.Filled.ReceiptLong,
                            onClick = {
                                viewModel.processEvent(HelpSupportViewModelIntent.ViewEvent.ToggleCategory(R.string.expenses))
                            }
                        )

                        SmallSuggestionCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(id = R.string.managing_push_alerts),
                            icon = Icons.Default.NotificationsActive,
                            onClick = {
                                viewModel.processEvent(HelpSupportViewModelIntent.ViewEvent.ToggleCategory(R.string.notifications))
                            }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.browse_by_category),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = customColors.titleTextColor
                        )
                    )

                    categories.forEach { category ->
                        FAQCategoryCard(
                            category = category,
                            isExpanded = expandedCategories.contains(category.titleResId),
                            onToggle = { viewModel.processEvent(HelpSupportViewModelIntent.ViewEvent.ToggleCategory(category.titleResId)) }
                        )
                    }
                }

                ContactSupportBanner()

                ReportAProblemCard()
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp)
                .clickable {
                    ComposeViewUtils.showToast(context, workUnderProgress)
                },
            shape = CircleShape,
            color = customColors.secondaryBackground,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ReportAProblemCard() {
    val customColors = LocalCustomColors.current
    var description by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.primaryBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.report_a_problem),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = customColors.titleTextColor
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.report_a_problem_subtitle),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = customColors.textColor.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.describe_what_went_wrong),
                        color = customColors.hintTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    unfocusedContainerColor = customColors.primaryBackground,
                    focusedContainerColor = customColors.primaryBackground,
                    unfocusedIndicatorColor = customColors.hintTextColor.copy(alpha = 0.2f),
                    focusedIndicatorColor = customColors.secondaryBackground
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clickable { /* Handle upload */ },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent,
                    border = borderWithDash(customColors.hintTextColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                            contentDescription = null,
                            tint = customColors.titleTextColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.upload_screenshot),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = customColors.titleTextColor,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Button(
                    onClick = { /* Handle send */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = customColors.secondaryBackground,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.send_report),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 14.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun borderWithDash(color: Color) = BorderStroke(
    width = 1.dp,
    color = color
)

@Composable
fun SmartSuggestionsPager(suggestions: List<SmartSuggestion>) {
    val pagerState = rememberPagerState(pageCount = { suggestions.size })

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(end = 40.dp),
        pageSpacing = 16.dp
    ) { page ->
        LargeSuggestionCard(suggestion = suggestions[page])
    }
}

@Composable
fun LargeSuggestionCard(suggestion: SmartSuggestion) {
    val customColors = LocalCustomColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.defaultImageCardColor.copy(alpha = 0.6f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column {
                Surface(
                    color = customColors.secondaryBackground,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.popular),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = suggestion.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = customColors.textColor,
                        lineHeight = 28.sp
                    )
                )
            }

            Icon(
                imageVector = suggestion.icon ?: Icons.Default.FlightTakeoff,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(48.dp),
                tint = customColors.textColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun FAQCategoryCard(
    category: FAQCategory,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.primaryBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(customColors.deepPurpleGlow.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            tint = customColors.secondaryBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = category.titleResId),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = customColors.titleTextColor
                        )
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationState),
                    tint = customColors.hintTextColor
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    category.faqs.forEach { faq ->
                        FAQItemView(faq = faq)
                    }
                }
            }
        }
    }
}

@Composable
fun FAQItemView(faq: FAQItem) {
    val customColors = LocalCustomColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = customColors.deepPurpleGlow.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = faq.question,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = customColors.titleTextColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = faq.answer,
            style = MaterialTheme.typography.bodySmall.copy(
                color = customColors.textColor,
                lineHeight = 20.sp
            )
        )
    }
}

@Composable
fun ContactSupportBanner() {
    val customColors = LocalCustomColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.secondaryBackground
        )
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.still_need_help),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.support_team_available),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }

            Button(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = { /* Contact Support */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = customColors.secondaryBackground
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.contact_support),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun HelpSupportToolbar(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = customColors.secondaryBackground
                )
            }
            Text(
                text = stringResource(id = R.string.help_support),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = customColors.secondaryBackground,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 16.sp
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = customColors.secondaryBackground
            )
        }
    }
}

@Composable
fun SmallSuggestionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    val customColors = LocalCustomColors.current
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.deepPurpleGlow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = customColors.textColor,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

package com.example.bbltripplanner.screens.user.general.viewModels

import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class HelpSupportViewModel : BaseMVIVViewModel<HelpSupportViewModelIntent.ViewEvent>() {
    private val _expandedCategories: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    val expandedCategories: StateFlow<Set<Int>> = _expandedCategories.asStateFlow()

    fun performSearch(categories: List<FAQCategory>, searchQuery: String) {
        val query = searchQuery.trim()
        if (query.isEmpty()) {
            return
        }

        val expandedSet = mutableSetOf<Int>()
        categories.forEach { category ->
            val matchesFaq = category.faqs.any { 
                it.question.contains(query, ignoreCase = true) || 
                it.answer.contains(query, ignoreCase = true) 
            }

            if (matchesFaq) {
                expandedSet.add(category.titleResId)
            }
        }
        _expandedCategories.value = expandedSet
    }

    fun toggleCategory(titleResId: Int) {
        val current = _expandedCategories.value
        if (current.contains(titleResId)) {
            _expandedCategories.value = current - titleResId
        } else {
            _expandedCategories.value = current + titleResId
        }
    }
    
    fun getFAQCategories(
        tripsIcon: androidx.compose.ui.graphics.vector.ImageVector,
        expensesIcon: androidx.compose.ui.graphics.vector.ImageVector,
        mediaIcon: androidx.compose.ui.graphics.vector.ImageVector,
        socialIcon: androidx.compose.ui.graphics.vector.ImageVector,
        notificationsIcon: androidx.compose.ui.graphics.vector.ImageVector,
        securityIcon: androidx.compose.ui.graphics.vector.ImageVector,
        issuesIcon: androidx.compose.ui.graphics.vector.ImageVector,
        contactIcon: androidx.compose.ui.graphics.vector.ImageVector
    ): List<FAQCategory> {
        return listOf(
            FAQCategory(
                R.string.trips_itinerary,
                tripsIcon,
                listOf(
                    FAQItem("How do I create a new trip?", "Go to the Home screen → Tap the “+” button → Select Create Trip → Add trip details like destination, dates, and members."),
                    FAQItem("Can multiple people edit the same trip?", "Yes, trips are collaborative. You can invite people and assign roles like viewer or editor."),
                    FAQItem("I can’t edit a trip. Why?", "You might not have edit permission. Ask the trip owner to grant you editing access."),
                    FAQItem("Can I make my trip private?", "Yes, you can set trip visibility to Private, Followers, or Public from Trip Settings.")
                )
            ),
            FAQCategory(
                R.string.expenses,
                expensesIcon,
                listOf(
                    FAQItem("How do I add expenses to a trip?", "Open the trip → Go to the Expenses section → Tap “Add Expense” → Enter details and split among members."),
                    FAQItem("Can I split expenses automatically?", "Yes, you can split equally or customize the split manually for each member."),
                    FAQItem("I added the wrong expense. Can I edit it?", "Yes, tap on the expense → Edit → Save changes.")
                )
            ),
            FAQCategory(
                R.string.media,
                mediaIcon,
                listOf(
                    FAQItem("Where are my trip photos stored?", "All photos and videos are stored inside the trip under the Media section."),
                    FAQItem("Why are my uploads slow?", "This depends on your network and upload quality settings. You can reduce upload quality in Settings → Data & Storage."),
                    FAQItem("Can I upload documents like tickets or PDFs?", "Yes, you can upload documents in the trip’s Documents section.")
                )
            ),
            FAQCategory(
                R.string.social_profile,
                socialIcon,
                listOf(
                    FAQItem("How does the feed work?", "Your home feed shows trips and updates from people you follow."),
                    FAQItem("Can I hide my trips from followers?", "Yes, you can control visibility in Privacy Settings or per trip."),
                    FAQItem("How do I follow or unfollow someone?", "Visit their profile → Tap Follow/Unfollow.")
                )
            ),
            FAQCategory(
                R.string.notifications,
                notificationsIcon,
                listOf(
                    FAQItem("I’m getting too many notifications. How do I stop them?", "Go to Settings → Notifications → Turn off specific notifications."),
                    FAQItem("Why didn’t I get a notification?", "Check if notifications are enabled in both app settings and your device settings.")
                )
            ),
            FAQCategory(
                R.string.account_security,
                securityIcon,
                listOf(
                    FAQItem("How do I reset my password?", "Go to Settings → Account → Change Password."),
                    FAQItem("How do I delete my account?", "Go to Settings → Scroll to Danger Zone → Tap Delete Account.")
                )
            ),
            FAQCategory(
                R.string.app_issues,
                issuesIcon,
                listOf(
                    FAQItem("The app is slow or crashing. What should I do?", "Try clearing cache from Settings → Data & Storage → Clear Cache. Also ensure you're on the latest version."),
                    FAQItem("My data is not syncing.", "Check your internet connection and try refreshing. If the issue persists, contact support.")
                )
            ),
            FAQCategory(
                R.string.help_support,
                contactIcon,
                listOf(
                    FAQItem("How can I contact support?", "Go to Help & Support → Contact Us → Fill out the form or email us."),
                    FAQItem("How long does support take to respond?", "Typically within 24–48 hours.")
                )
            )
        )
    }

    override fun processEvent(viewEvent: HelpSupportViewModelIntent.ViewEvent) {
        when (viewEvent) {
            is HelpSupportViewModelIntent.ViewEvent.PerformSearch -> performSearch(viewEvent.faqCategories, viewEvent.searchQuery)
            is HelpSupportViewModelIntent.ViewEvent.ToggleCategory -> toggleCategory(viewEvent.category)
        }
    }
}

data class FAQItem(
    val question: String,
    val answer: String
)

data class FAQCategory(
    val titleResId: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val faqs: List<FAQItem>
)

data class SmartSuggestion(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null
)


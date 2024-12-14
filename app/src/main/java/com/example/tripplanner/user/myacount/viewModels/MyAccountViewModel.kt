package com.example.tripplanner.user.myacount.viewModels

import androidx.lifecycle.ViewModel
import com.example.tripplanner.R
import com.example.tripplanner.common.Constants
import com.example.tripplanner.user.myacount.entity.ProfileActionItem

class MyAccountViewModel(

) : ViewModel() {
    val profileActionList by lazy {
        listOf(
            ProfileActionItem(Constants.PROFILE_DETAILS, R.drawable.ic_profile_details_action, R.string.profile_details, R.string.profile_details_subtitle),
            ProfileActionItem(Constants.NOTIFICATIONS, R.drawable.ic_notification_action, R.string.notifications, R.string.notifications_subtitle),
            ProfileActionItem(Constants.SETTINGS, R.drawable.ic_settings_action, R.string.settings, R.string.settings_subtitle),
            ProfileActionItem(Constants.HELP_SUPPORT, R.drawable.ic_helpsupport_action, R.string.help_support, R.string.help_support_subtitle),
            ProfileActionItem(Constants.LOGOUT, R.drawable.ic_logout_action, R.string.logout, R.string.logout_subtitle)
        )
    }
}
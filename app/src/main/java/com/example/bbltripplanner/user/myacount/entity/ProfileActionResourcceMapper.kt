package com.example.bbltripplanner.user.myacount.entity

import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants

object ProfileActionResourceMapper {
    fun getProfileActions(): List<ProfileActionItem> {
        return listOf(
            ProfileActionItem(Constants.PROFILE_DETAILS, R.drawable.ic_profile_details_action, R.string.profile_details, R.string.profile_details_subtitle),
            ProfileActionItem(Constants.NOTIFICATIONS, R.drawable.ic_notification_action, R.string.notifications, R.string.notifications_subtitle),
            ProfileActionItem(Constants.SETTINGS, R.drawable.ic_settings_action, R.string.settings, R.string.settings_subtitle),
            ProfileActionItem(Constants.HELP_SUPPORT, R.drawable.ic_helpsupport_action, R.string.help_support, R.string.help_support_subtitle),
            ProfileActionItem(Constants.LOGOUT, R.drawable.ic_logout_action, R.string.logout, R.string.logout_subtitle)
        )
    }
}
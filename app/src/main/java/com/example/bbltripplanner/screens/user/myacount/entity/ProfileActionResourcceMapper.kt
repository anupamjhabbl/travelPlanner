package com.example.bbltripplanner.screens.user.myacount.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Forum
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants

object ProfileActionResourceMapper {
    fun getAccountActions(): List<ProfileActionItem> {
        return listOf(
            ProfileActionItem(Constants.PROFILE_DETAILS, R.drawable.ic_profile_details_action, R.string.profile_details, R.string.profile_details_subtitle),
            ProfileActionItem(Constants.NOTIFICATIONS, R.drawable.ic_notification_action, R.string.notifications, R.string.notifications_subtitle),
            ProfileActionItem(Constants.SETTINGS, R.drawable.ic_settings_action, R.string.settings, R.string.settings_subtitle),
            ProfileActionItem(Constants.HELP_SUPPORT, R.drawable.ic_helpsupport_action, R.string.help_support, R.string.help_support_subtitle),
            ProfileActionItem(Constants.LOGOUT, R.drawable.ic_logout_action, R.string.logout, R.string.logout_subtitle)
        )
    }

    fun getMyProfileActions(): List<ProfileActionItem> {
        return listOf(
            ProfileActionItem(Constants.TRIP_PAGE, R.drawable.ic_trips, R.string.your_trips, 0),
            ProfileActionItem(Constants.FAVOURITES, R.drawable.ic_favourites_filled, R.string.favouriees, 0),
            ProfileActionItem(Constants.REVIEW_PAGES, R.drawable.ic_threads, R.string.your_story, 0),
            ProfileActionItem(Constants.BUZZ_PAGE, R.drawable.ic_buzz_filled, R.string.your_buzz, 0),
            ProfileActionItem(Constants.CONTACTS, R.drawable.ic_contacts, R.string.social, 0)
        )
    }

    fun getOtherProfileActions(): List<ProfileActionItem> {
        return listOf(
            ProfileActionItem(Constants.TRIP_PAGE, R.drawable.ic_trips, R.string.user_trips, 0),
            ProfileActionItem(Constants.REVIEW_PAGES, R.drawable.ic_threads, R.string.user_story, 0),
            ProfileActionItem(Constants.BUZZ_PAGE, R.drawable.ic_buzz_filled, R.string.user_buzz, 0),
            ProfileActionItem(Constants.CONTACTS, R.drawable.ic_contacts, R.string.contacts, 0)
        )
    }

    fun getVaultMenuItems(): List<VaultMenuItem> {
        return listOf(
            VaultMenuItem(Constants.TRIP_PAGE, Icons.Default.FlightTakeoff, R.string.vault_trip_title, R.string.vault_trip_sub_title),
            VaultMenuItem(Constants.FAVOURITES, Icons.Default.Favorite,R.string.vault_fav_title, R.string.vault_fav_sub_title),
            VaultMenuItem(Constants.BUZZ_PAGE, Icons.Default.Forum,R.string.vault_buzz_title, R.string.vault_buzz_sub_title)
        )
    }
}
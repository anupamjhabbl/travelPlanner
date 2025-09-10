package com.example.bbltripplanner.screens.posting.entity

import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants

object TripActionResourceMapper {
    fun getTripActions(): List<TripActionItem> {
        return listOf(
            TripActionItem(Constants.TripDetailScreen.GENERAL, R.drawable.ic_general, R.string.general, R.string.general_subtitle),
            TripActionItem(Constants.TripDetailScreen.ATTACHMENTS, R.drawable.ic_attachment, R.string.attachments, R.string.attachments_subtitle),
            TripActionItem(Constants.TripDetailScreen.EXPENSES, R.drawable.ic_expenses, R.string.expenses, R.string.expenses_subtitle),
            TripActionItem(Constants.TripDetailScreen.ITINERARY, R.drawable.ic_itinerary, R.string.itinerary, R.string.itinerary_subtitle),
            TripActionItem(Constants.TripDetailScreen.GROUP, R.drawable.ic_group, R.string.group, R.string.group_subtitle)
        )
    }
}
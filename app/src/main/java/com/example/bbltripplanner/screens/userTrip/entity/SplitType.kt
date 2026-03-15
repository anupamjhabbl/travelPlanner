package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector

enum class SplitType(
    val value: String,
    val icon: ImageVector
) {
    EVERYONE("Everyone", Icons.Default.Public),
    SINGLE("Single", Icons.Default.Person),
    GROUP("Group", Icons.Default.Groups);
    
    companion object {
        fun getEnum(value: String): SplitType {
            return SplitType.entries.find {
                it.value == value
            } ?: EVERYONE
        }
    }
}
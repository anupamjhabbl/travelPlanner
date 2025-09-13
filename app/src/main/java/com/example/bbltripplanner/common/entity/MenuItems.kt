package com.example.bbltripplanner.common.entity

object MenuItems {
    enum class MyProfileMenuItem(val value: String) {
        EDIT("Edit"), SHARE("Share")
    }

    enum class OtherProfileMenuItem(val value: String) {
        SHARE("Share"), BLOCK("Block")
    }
}
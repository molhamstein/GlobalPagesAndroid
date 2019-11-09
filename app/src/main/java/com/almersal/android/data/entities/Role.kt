package com.almersal.android.data.entities

data class Role(
    val key: String,
    val name: String,
    val privileges: List<String>
)
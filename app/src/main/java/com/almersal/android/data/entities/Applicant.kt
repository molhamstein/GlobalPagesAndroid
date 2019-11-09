package com.almersal.android.data.entities

data class Applicant(
    val createdAt: String,
    val id: String,
    val jobId: String,
    val status: String,
    val user: User,
    val userId: String
)
package com.almersal.android.data.entities

data class SentUpdateProfile @JvmOverloads constructor(
    var username: String? = null,
    var imageProfile: String = "",
    var phoneNumber: String = "",
    var behanceLink: String? = null,
    var bio: String? = null,
    var city: City? = null,
    var cityId: String? = null,
    var creationDate: String? = null,
    var cvURL: String? = null,
    var education: MutableList<Education>? = null,
    var experience: MutableList<Experience>? = null,
    var facebookLink: String? = null,
    var githubLink: String? = null,
    var id: String? = null,
    var primaryIdentifier: String? = null,
    var tags: MutableList<String>? = null,
    var skills: MutableList<Tag>? = null,
    var twitterLink: String? = null,
    var userId: String? = null,
    var websiteLink: String? = null
)
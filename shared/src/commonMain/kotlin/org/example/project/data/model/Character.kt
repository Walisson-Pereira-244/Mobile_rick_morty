package org.example.project.data.model

import kotlinx.serialization.Serializable


@Serializable
data class CharacterResponse(
    val info: PageInfo,
    val results: List<Character>,
)

@Serializable
data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String = "",
    val gender: String,
    val origin: Place,
    val location: Place,
    val image: String,
    val episode: List<String> = emptyList(),
    val created: String = "",
)

@Serializable
data class Place(
    val name: String,
    val url: String = "",
)

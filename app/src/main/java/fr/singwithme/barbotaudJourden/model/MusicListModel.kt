package fr.singwithme.barbotaudJourden.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicListModel (
    val name: String,
    val artist: String,
    val locked: Boolean?,
    val path: String?
)
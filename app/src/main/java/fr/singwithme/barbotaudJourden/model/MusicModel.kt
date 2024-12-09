package fr.singwithme.barbotaudJourden.model

import kotlin.time.Duration

data class LyricModel(
    var duration: Duration,
    var text: String
)

data class MusicModel(
    var title: String,
    var author: String,
    var soundTrack: String?,
    var lyrics: List<LyricModel>
)
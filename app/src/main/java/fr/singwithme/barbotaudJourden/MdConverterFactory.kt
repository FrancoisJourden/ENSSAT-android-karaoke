package fr.singwithme.barbotaudJourden

import fr.singwithme.barbotaudJourden.model.LyricModel
import fr.singwithme.barbotaudJourden.model.MusicModel
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MdConverterFactory : Converter.Factory() {
    private fun convertDuration(duration: String): Duration {
        val split = duration.substring(2, duration.length - 2).split(":")
        return split[0].toInt().minutes + split[1].toInt().seconds
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody?, MusicModel>? {
        return Converter { responseBody ->
            var responseString = responseBody!!.string()

            val lines = responseString.split("\n").filter { it.isNotEmpty() }
            val parsed: MutableMap<String, MutableList<String>> = mutableMapOf()

            var currentTitle = ""
            for (i in 0..lines.size - 1) {
                val line = lines[i]
                if (line.startsWith("#")) {
                    currentTitle = line.substring(1).trim()
                } else {
                    if (!parsed.containsKey(currentTitle)) {
                        parsed[currentTitle] = mutableListOf()
                    }
                    parsed[currentTitle]?.add(line)
                }
            }

            if (!parsed.containsKey("title") || parsed["title"] == null || parsed["title"]?.size == 0) {
                throw Exception("No title found")
            }
            if (!parsed.containsKey("author") || parsed["author"] == null || parsed["author"]?.size == 0) {
                throw Exception("No author found")
            }
            if (!parsed.containsKey("soundtrack") || parsed["soundtrack"] == null || parsed["soundtrack"]?.size == 0) {
                throw Exception("No soundtrack found")
            }
            if (!parsed.containsKey("lyrics") || parsed["lyrics"] == null || parsed["lyrics"]?.size == 0) {
                throw Exception("No lyrics found")
            }

            var parsedLyrics: MutableList<LyricModel> = emptyList<LyricModel>().toMutableList()

            var previousDuration = Duration.ZERO
            for (i in 0..parsed["lyrics"]!!.size - 1) {
                var texts = parsed["lyrics"]!![i].split("\\{ \\d+:\\d+ \\}".toRegex())
                    .filter { it.isNotEmpty() }
                var durations =
                    "\\{ \\d+:\\d+ \\}".toRegex().findAll(parsed["lyrics"]!![i]).map {
                        convertDuration(it.value)
                    }.toMutableList()

                if (texts.isNotEmpty() && durations.size <= texts.size) {
                    durations.add(
                        "\\{ \\d+:\\d+ \\}".toRegex().findAll(parsed["lyrics"]!![i + 1]).first()
                            .let { convertDuration(it.value) }
                    )
                }

                parsedLyrics.add(
                    LyricModel(
                        duration = durations[0].minus(previousDuration),
                        text = ""
                    )
                )
                for (j in 0..texts.size - 1) {
                    val duration = durations[j + 1] - durations[j]
                    parsedLyrics.add(LyricModel(duration = duration, text = texts[j]))
                }
                previousDuration = durations[durations.size - 1]
            }

            MusicModel(
                title = parsed["title"]!![0],
                author = parsed["author"]!![0],
                soundTrack = parsed["soundtrack"]!![0],
                lyrics = parsedLyrics,
            )
        }
    }
}


package fr.singwithme.barbotaudJourden

import fr.singwithme.barbotaudJourden.MdConverterFactory
import fr.singwithme.barbotaudJourden.model.LyricModel
import fr.singwithme.barbotaudJourden.model.MusicModel
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.time.Duration.Companion.seconds

class MdConverterFactoryTest {
    private val factory = MdConverterFactory()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Test
    fun `test convert valid response`() {
        val responseBody = ResponseBody.create(
            MediaType.get("text/plain"),
            """
            #title
            Test Title
            #author
            Test Author
            #soundtrack
            test.mp3
            #lyrics
            { 00:00 }Line 1{ 00:05 }
            { 00:05 }Line 2{ 00:10 }
            { 00:10 }Line 3{ 00:15 }
            """.trimIndent()
        )

        val converter = factory.responseBodyConverter(MusicModel::class.java, arrayOf(), retrofit)
        val result = converter?.convert(responseBody)

        val expectedLyrics = listOf(
            LyricModel(0.seconds, ""),
            LyricModel(5.seconds, "Line 1"),
            LyricModel(0.seconds, ""),
            LyricModel(5.seconds, "Line 2"),
            LyricModel(0.seconds, ""),
            LyricModel(5.seconds, "Line 3")
        )

        val expected = MusicModel(
            title = "Test Title",
            author = "Test Author",
            soundTrack = "test.mp3",
            lyrics = expectedLyrics
        )

        assertEquals(expected, result)
    }

    @Test
    fun `test convert invalid response no title`() {
        val responseBody = ResponseBody.create(
            MediaType.get("text/plain"),
            """
            #author
            Test Author
            #soundtrack
            test.mp3
            #lyrics
            { 00:00 }Line 1{ 00:05 }
            { 00:05 }Line 2{ 00:10 }
            { 00:10 }Line 3{ 00:15 }
            """.trimIndent()
        )

        val converter = factory.responseBodyConverter(MusicModel::class.java, arrayOf(), retrofit)
        assertThrows(Exception::class.java) {
            converter?.convert(responseBody)
        }
    }
}
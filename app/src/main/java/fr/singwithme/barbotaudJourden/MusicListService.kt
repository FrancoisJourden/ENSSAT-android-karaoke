package fr.singwithme.barbotaudJourden

import fr.singwithme.barbotaudJourden.model.MusicListModel
import fr.singwithme.barbotaudJourden.model.MusicModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

private const val MUSIC_LIST_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(MUSIC_LIST_URL)
    .build()

private val retrofitMd = Retrofit.Builder()
    .addConverterFactory(MdConverterFactory())
    .baseUrl(MUSIC_LIST_URL)
    .build()

interface MusicListService{
    @GET("playlist.json")
    fun getMusics(): Call<List<MusicListModel>>
}

interface MusicDetailsService{
    @GET
    fun getMusic(@Url url: String): Call<MusicModel>
}


object MusicListApi{
    val retrofitService: MusicListService by lazy{
        retrofitJson.create(MusicListService::class.java)
    }
}

object MusicDetailApi{
    val retrofitService: MusicDetailsService by lazy{
        retrofitMd.create(MusicDetailsService::class.java)
    }
}
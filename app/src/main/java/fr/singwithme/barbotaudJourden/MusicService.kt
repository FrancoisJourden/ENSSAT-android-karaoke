package fr.singwithme.barbotaudJourden

import fr.singwithme.barbotaudJourden.model.MusicListModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val MUSIC_LIST_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(MUSIC_LIST_URL)
    .build()

interface MusicService{
    @GET("playlist.json")
    fun getMusics(): Call<List<MusicListModel>>
}


object MusicApi{
    val retrofitService: MusicService by lazy{
        retrofit.create(MusicService::class.java)
    }
}
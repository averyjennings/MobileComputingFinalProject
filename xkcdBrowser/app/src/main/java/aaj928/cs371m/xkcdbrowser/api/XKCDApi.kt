package aaj928.cs371m.xkcdbrowser.api

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface XKCDApi {


    @GET("/{num}/info.0.json")
    suspend fun fetchComic(
        @Path("num") num : String): Comic


    @GET("/info.0.json")
    suspend fun fetchCurrentComic(): Comic

    companion object {
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
            return GsonConverterFactory.create(gsonBuilder.create())
        }
        //private const val BASE_URL = "https://www.xkcd.com/"
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("www.xkcd.com")
            .build()
        fun create(): XKCDApi = create(httpurl)
        private fun create(httpUrl: HttpUrl): XKCDApi {

            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(XKCDApi::class.java)
        }
    }
}
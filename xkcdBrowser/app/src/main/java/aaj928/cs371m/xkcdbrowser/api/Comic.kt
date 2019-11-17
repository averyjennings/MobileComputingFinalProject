package aaj928.cs371m.xkcdbrowser.api

import com.google.gson.annotations.SerializedName

data class Comic (
    @SerializedName("month")
    val month: String?,
    @SerializedName("num")
    val num: Int?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("year")
    val year: String?,
    @SerializedName("news")
    val news: String?,
    @SerializedName("safe_title")
    val safeTitle: String?,
    @SerializedName("transcript")
    val transcript: String?,
    @SerializedName("alt")
    val alt : String?,
    @SerializedName("img")
    val imageUrl : String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("day")
    val day: String?
) {
}

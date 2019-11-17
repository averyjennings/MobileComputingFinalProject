package aaj928.cs371m.xkcdbrowser.api

class ComicPostRepository(private val api: XKCDApi) {

    fun unpackComic(response: XKCDApi.ComicResponse): Comic {
            val comic = Comic(
                response.data.month,
                response.data.num,
                response.data.link,
                response.data.year,
                response.data.news,
                response.data.safeTitle,
                response.data.transcript,
                response.data.alt,
                response.data.imageUrl,
                response.data.title,
                response.data.day
                )

        return comic
    }

    suspend fun getAllComics(): List<Comic>? {
        var list = mutableListOf<Comic>()
        //TODO for now this only fetches 10 comics
        for (i in 1..10) {
            list.add(api.fetchComic(i.toString()))//unpackComic(api.fetchComic(i.toString())))
        }
        return list
    }

   /* suspend fun getComic(num : String): Comic {
        if(num.length==0) {
            return unpackComic(api.fetchCurrentComic())
        }else{
            return unpackComic(api.fetchComic("num"))
        }
    }*/
}

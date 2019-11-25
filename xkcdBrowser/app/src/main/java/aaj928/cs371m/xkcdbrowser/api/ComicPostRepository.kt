package aaj928.cs371m.xkcdbrowser.api

class ComicPostRepository(private val api: XKCDApi) {

    //fetch comcics from initial index to initial+length
    suspend fun getNextComics(tempList : ArrayList<Comic>, init: Int, len : Int): ArrayList<Comic>? {
        var list = arrayListOf<Comic>()
        if(len == 0){
            list.add(api.fetchComic(init.toString()))
            tempList.addAll(list)
            return tempList
        }

        for (i in init..init+len) {
            if(i == 404){
                continue
            }
            list.add(api.fetchComic(i.toString()))
        }
        tempList.addAll(list)
        return tempList
    }

    suspend fun getFirstComics(/*initial : Int*/): ArrayList<Comic>? {
        var list = arrayListOf<Comic>()

        //var lastComic = api.fetchCurrentComic()
        val last = 10//lastComic.num!!

        for (i in 1..last) {
            list.add(api.fetchComic(i.toString()))
        }

        return list
    }

    suspend fun getAllComics(/*initial : Int*/): ArrayList<Comic>? {
        var list = arrayListOf<Comic>()

        var lastComic = api.fetchCurrentComic()
        val last = lastComic.num!!

        for (i in 1..403) {
            list.add(api.fetchComic(i.toString()))
        }
        for(i in 405..last){
            list.add(api.fetchComic(i.toString()))
        }
        return list
    }

    suspend fun getComic(num : String): Comic {
        if(num.length==0) {
            return api.fetchCurrentComic()
        }else{
            return api.fetchComic("num")
        }
    }
}
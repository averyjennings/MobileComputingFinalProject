package aaj928.cs371m.xkcdbrowser.api

import java.util.*
import kotlin.collections.ArrayList

class ComicPostRepository(private val api: XKCDApi) {

    private lateinit var lastComic : Comic

    //makes an entirely new list with only one comic in it now.
    suspend fun jumpToComic(index : Int, newest : Boolean) : LinkedList<Comic>? {
        lastComic = getComic("")
        if(index > lastComic.num!!){
            return null
        }

        var list = LinkedList(listOf<Comic>())
        //list.add(api.fetchComic((index-1).toString()))
        if(newest){
            list.add(api.fetchCurrentComic())
        }
        else {
            list.add(api.fetchComic((index).toString()))
        }
        //list.add(api.fetchComic((index+1).toString()))

        return list
    }


    suspend fun getPrevComic(tempList : LinkedList<Comic>, comicNum : Int) : LinkedList<Comic>? {
        if(comicNum == 0){
            return null
        }
        var comic : Comic
        if(comicNum == 404){
            comic = getComic((comicNum-1).toString())
        }
        else{
            comic = getComic((comicNum).toString())
        }
        if(!tempList.contains(comic)) {
            tempList.addFirst(comic)
        }
        return tempList
    }
    suspend fun getNextComic(tempList : LinkedList<Comic>, comicNum : Int) : LinkedList<Comic>? {
        if(comicNum == 0 || comicNum > lastComic.num!!){
            return null
        }
        var comic : Comic
        if(comicNum == 404){
            comic = getComic((comicNum+1).toString())
        }
        else{
            comic = getComic((comicNum).toString())
        }
        if(!tempList.contains(comic)) {
            tempList.add(comic)
        }
        return tempList
    }
    //fetch comcics from initial index to initial+length
    suspend fun getNextComics(tempList : LinkedList<Comic>, init: Int, len : Int): LinkedList<Comic>? {
        if(len == 0){//one item
            var comic = api.fetchComic(init.toString())
            if(!tempList.contains(comic)){
                tempList.add(comic)
            }
            return tempList
        }

        for (i in init..init+len) {
            if(i == 404){
                continue
            }
            var comic = api.fetchComic(i.toString())
            if(!tempList.contains(comic)){
                tempList.add(comic)
            }
        }
        return tempList
    }


    suspend fun getAllComics(/*initial : Int*/): LinkedList<Comic>? {
        var list = LinkedList(listOf<Comic>())

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
            return api.fetchComic(num)
        }
    }
}
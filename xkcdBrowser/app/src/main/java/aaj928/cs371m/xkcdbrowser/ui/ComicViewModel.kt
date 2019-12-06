package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.api.Comic
import aaj928.cs371m.xkcdbrowser.api.ComicPostRepository
import aaj928.cs371m.xkcdbrowser.api.XKCDApi


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ComicViewModel : ViewModel() {


    private val favorites = HashMap<Int, Comic>() //key: num, val: Comic
    private var favoritesLiveData = MutableLiveData<HashMap<Int, Comic>>()

    private val api = XKCDApi.create()
    private val repository = ComicPostRepository(api)

    private val comics = MutableLiveData<LinkedList<Comic>>()

    private val nextComic = MutableLiveData<Comic>() //This is the most recently fetched comic that needs to be added to the pageradapter
    private val prevComic = MutableLiveData<Comic>()

    private var lastComic = MutableLiveData<Comic>()



    fun isSanitary(n: Int) : Boolean{
        if(lastComic.value == null){
            refreshLastComic()
        }
        if(n < 1 || n == 404){
            return false
        }
        if(lastComic.value != null && n > lastComic.value?.num!!)
        {
            return false
        }

        return true
    }

    fun refreshLastComic() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {

        lastComic.postValue(repository.getComic(""))
    }

    fun getFirst() : Comic{
        return comics.value!!.first
    }

    fun getLast() : Comic{
        return comics.value!!.last
    }

    fun addFavorite(key : Int) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(!favorites.containsKey(key)) {
            favorites.put(key, repository.getComic(key.toString()))
            favoritesLiveData.postValue(favorites)
        }
    }

    fun removeFavorite(key : Int){
        favorites.remove(key)
        favoritesLiveData.postValue(favorites)
    }

    fun isFavorited(key : Int) : Boolean{
        return favorites.contains(key)
    }

    fun getFavoritesAsList() : List<Comic> {
        return favorites.values.toList()
    }


    fun observeNextComic() : LiveData<Comic>{
        return nextComic
    }

    fun observePrevComic() : LiveData<Comic>{
        return prevComic
    }

    fun jumpToNewest() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {

        refreshLastComic().join()

        //var list = LinkedList(listOf<Comic>())
        //list.add(lastComic.value)
        comics.postValue(repository.jumpToComic(0, true))

        //nextComic.postValue(null)
        //getPrev().join()
    }

    fun jumpToComic(index : Int) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(!isSanitary(index)){
            return@launch
        }
        comics.postValue(repository.jumpToComic(index, false) ?: comics.value)
        //TODO
        //prevComic.postValue(null)
        //nextComic.postValue(null)
        //getPrev().invokeOnCompletion { /*updatePrev()*/ }
        //getNext().invokeOnCompletion { /*updateNext()*/ }
        //getPrev().join()
        //getNext().join()
    }

    //updates the prevComic
    fun updatePrev() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        /*if(prevComic.value?.num == comics.value?.first?.num){
            return@launch
        }*/
        if(comics.value != null){
            prevComic.postValue(comics.value!!.first)
        }
    }

    fun updateNext() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        /*if(nextComic.value?.num == comics.value?.last?.num){
            return@launch
        }*/
        if(comics.value != null) {
            nextComic.postValue(comics.value!!.last)
        }
    }

    fun getPrev() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(comics.value != null) {
            if (comics.value!!.first.num!! <= 1) {
                return@launch
            }
        }else{
            return@launch
        }
        if(prevComic?.value?.num != comics.value?.first?.num){
            println("inside getPrev but shouldn't have called getPrev")
            return@launch
        }

        var tempList = LinkedList(listOf<Comic>())
        tempList.addAll(comics.value ?: LinkedList(listOf<Comic>()))
        comics.postValue(repository.getPrevComic(tempList, tempList.peekFirst().num!!-1) ?: tempList)
        //prevComic.postValue(comics.value!!.peekFirst())
    }

    fun getNext() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(comics.value == null){
            return@launch
        }

        var tempList = LinkedList(listOf<Comic>())
        tempList.addAll(comics.value ?: LinkedList(listOf<Comic>()))

        if(tempList.size > 0){
            if(tempList.peekLast().num == lastComic.value?.num){
                return@launch
            }
        }

        comics.postValue(repository.getNextComic(tempList, tempList.peekLast().num!!+1) ?: tempList)
        //nextComic.postValue(comics.value!!.peekLast())
    }


    fun netRefresh() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        comics.postValue(repository.getAllComics())
    }

    fun getNextComics(init : Int, len : Int) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {

        //TODO some logic to append the next comics onto the list (make sure it doesn't go past the last comic)
        var tempList = LinkedList(listOf<Comic>())
        tempList.addAll(comics.value ?: LinkedList(listOf<Comic>()))
        comics.postValue(repository.getNextComics(tempList, init, len))

    }


    fun observeComics() : LiveData<LinkedList<Comic>> {
        return comics
    }


    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {

            val intent = Intent(context, OnePost::class.java)

            intent.putExtra("title", redditPost.title)
            intent.putExtra("selfText", redditPost.selfText)
            intent.putExtra("imageURL", redditPost.imageURL)
            intent.putExtra("thumbnailURL", redditPost.thumbnailURL)

            context.startActivity(intent)
        }
    }*/
}
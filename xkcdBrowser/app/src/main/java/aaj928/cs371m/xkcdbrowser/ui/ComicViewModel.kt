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



    private val api = XKCDApi.create()
    private val repository = ComicPostRepository(api)

    private val comics = MutableLiveData<LinkedList<Comic>>()

    private val nextComic = MutableLiveData<Comic>() //This is the most recently fetched comic that needs to be added to the pageradapter
    private val prevComic = MutableLiveData<Comic>()


    fun observeNextComic() : LiveData<Comic>{
        return nextComic
    }

    fun observePrevComic() : LiveData<Comic>{
        return prevComic
    }

    fun jumpToComic(index : Int) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        comics.postValue(repository.jumpToComic(index))
    }

    //updates the prevComic
    fun updatePrev() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(comics.value != null){
            prevComic.postValue(comics.value!!.first)
        }
    }

    fun updateNext() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(comics.value != null) {
            nextComic.postValue(comics.value!!.last)
        }
    }

    fun getPrev() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        if(comics.value != null) {
            if (comics.value!!.first.num!! == 0) {
                return@launch
            }
        }else{
            return@launch
        }

        var tempList = LinkedList(listOf<Comic>())
        tempList.addAll(comics.value ?: LinkedList(listOf<Comic>()))
        comics.postValue(repository.getPrevComic(tempList, tempList.peekFirst().num!!-1))
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
        comics.postValue(repository.getNextComics(tempList, tempList.peekLast().num!!+1, 0))
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
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

class ComicViewModel : ViewModel() {



    private val api = XKCDApi.create()
    private val repository = ComicPostRepository(api)

    private val comics = MutableLiveData<ArrayList<Comic>>()


    fun netRefresh() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        comics.postValue(repository.getAllComics())
    }

    fun getNextComics(init : Int, len : Int) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {

        //TODO some logic to append the next comics onto the list (make sure it doesn't go past the last comic)
        var tempList = arrayListOf<Comic>()
        tempList.addAll(comics.value ?: arrayListOf<Comic>())
        comics.postValue(repository.getNextComics(tempList, init, len))


    }
    fun getFirstComics() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        comics.postValue(repository.getFirstComics())
    }


    fun observeComics() : LiveData<ArrayList<Comic>> {
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
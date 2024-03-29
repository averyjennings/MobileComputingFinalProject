package aaj928.cs371m.xkcdbrowser

import aaj928.cs371m.xkcdbrowser.api.Comic
import aaj928.cs371m.xkcdbrowser.ui.ComicFragment
import aaj928.cs371m.xkcdbrowser.ui.ComicPagerAdapter
import aaj928.cs371m.xkcdbrowser.ui.ComicViewModel
import aaj928.cs371m.xkcdbrowser.ui.FavoritesFragment
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import kotlinx.android.synthetic.main.activity_main.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import androidx.core.content.ContextCompat.getSystemService
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_comic.*
import okhttp3.internal.notify
import java.lang.Double.parseDouble


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ComicPagerAdapter
    private lateinit var viewModel: ComicViewModel

    private lateinit var favorites : FavoritesFragment

    private var savedComicNum = 0
    private var inFavorites = false

    private var newestFlag = false
    private var jumpedFlag = false

    /*private fun titleSearch() {
        var search = supportActionBar!!.customView.findViewById<EditText>(R.id.actionSearch)
        search.addTextChangedListener{
            val s : String = it.toString()
            val filteredList = arrayListOf<RedditPost>()
            viewModel.observeSearchPosts().observe(this, Observer {
                for(post in it){
                    if(post.title.toLowerCase().contains(s.toLowerCase())){
                        filteredList.add(post)
                    }
                }
            })
            postRowAdapter.submitList(filteredList)
            postRowAdapter.notifyDataSetChanged()
        }
    }*/

    private fun initActionBar(actionBar: ActionBar) {
        // Disable the default and enable the custom
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        val customView: View =
            layoutInflater.inflate(R.layout.action_bar, null)
        // Apply the custom view
        var editText = customView.findViewById<EditText>(R.id.actionSearch)
        editText.setOnClickListener{
            editText.isCursorVisible = true
        }

        editText.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(
                v: TextView, actionId: Int,
                event: KeyEvent?
            ): Boolean {

                if(actionId == EditorInfo.IME_ACTION_DONE || event?.getAction() == ACTION_DOWN
                    || event?.getAction() == KEYCODE_ENTER) {
                    val s = v.text.toString()
                    jumpToComic(s)
                    return true
                }

                editText.isCursorVisible = false
                hideKeyboard()
                return false
            }
        })

        actionBar.customView = customView

        favorites = FavoritesFragment()//.newInstance()
        var heart = actionBar.customView.findViewById<ImageView>(R.id.actionFavorite)
        heart.setOnClickListener{
            /*supportFragmentManager
                .beginTransaction()
                // No back stack for home
                .replace(R.id.main_frame, favorites)
                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit()*/
            if(!inFavorites){
                inFavorites=true
                savedComicNum=(viewPager.adapter as ComicPagerAdapter).getComic(viewPager.currentItem).num!!
                pagerAdapter = ComicPagerAdapter(supportFragmentManager/*, it*/, viewModel)
                for(c : Comic in viewModel.getFavoritesAsList()){
                    pagerAdapter.addComic(c)
                }
                viewPager.adapter = pagerAdapter

            }else{
                inFavorites=false
                println("saved comic: "+savedComicNum.toString())
                jumpToComic(savedComicNum.toString())
            }
        }
        var newBut = actionBar.customView.findViewById<ImageView>(R.id.newBut)
        newBut.setOnClickListener{
            jumpToNewest()
        }

    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }

    fun isSanitary(s: String) : Boolean{
        val n : Int = s.toIntOrNull() ?: return false

        //TODO CHECK UPPPER BOUND
        if(n > 0  && n != 404){
            return true
        }

        return false
    }

    fun jumpToNewest(){
        if(!inFavorites) {
            if (viewPager != null) {//
                viewPager.adapter = null
            }
            newestFlag=true
            //viewModel.refreshLastComic().invokeOnCompletion {}
            viewModel.jumpToNewest().invokeOnCompletion {
                //viewModel.getPrev()
                //viewModel.updatePrev()
            }
            viewPager.setCurrentItem(1)

        }
    }

    fun jumpToComic(num : String){
        if(!isSanitary(num)){
            return
        }
        if(!inFavorites) {
            if (viewPager != null) {
                viewPager.adapter = null
            }
            jumpedFlag=true
            viewModel.jumpToComic(num.toInt()).invokeOnCompletion {
                //viewModel.getPrev()
                //viewModel.getNext()
            }
        }else{
            if(viewModel.isFavorited(num.toInt())){
                val pos = (viewPager.adapter as ComicPagerAdapter).positionOf(num.toInt())
                if(pos == -1){
                    return
                }else{
                    viewPager.setCurrentItem(pos)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
        }

        initActionBar(supportActionBar!!)

        viewModel = ViewModelProviders.of(this)[ComicViewModel::class.java]
        viewPager = findViewById(R.id.viewPager)

        viewModel.observeComics().observe(this, Observer {
            if(!inFavorites) {
                if (viewPager.adapter == null) {
                    pagerAdapter = ComicPagerAdapter(supportFragmentManager/*, it*/, viewModel)
                    for (c: Comic in it) {
                        pagerAdapter.addComic(c)
                    }
                    viewPager.adapter = pagerAdapter

                    if (it.first == it.last) {
                        viewModel.getPrev()
                        viewModel.getNext()
                    }

                    if(newestFlag){
                        newestFlag = false
                        viewPager.setCurrentItem(1)
                    }

                    if(it.isEmpty()){
                        jumpToNewest()
                    }


                }

                //viewModel.getNext()
                //viewModel.getPrev()
                /*if(viewPager.currentItem==0){
                viewModel.getPrev()
            }else{

            }*/

                /*if(viewPager.currentItem==0){
                viewModel.getPrev()
                viewModel.updatePrev()
            }else if(viewPager.currentItem == (viewPager.adapter as ComicPagerAdapter).getCount()-1) {
                viewModel.updateNext()
            }*/

                viewModel.updatePrev()
                viewModel.updateNext()
            }
            if(jumpedFlag){
                jumpedFlag=false
                if(pagerAdapter.getComic(0).num!! != 1) {
                    viewPager.setCurrentItem(1)
                }
                //viewModel.getPrev()
                //viewModel.getNext()
            }


        })


        //TODO FIX THESE BUGS!!!

        viewModel.observeNextComic().observe(this, Observer {
            if(it != null) {
                if (viewPager.currentItem == ((viewPager.adapter as ComicPagerAdapter).getCount() - 1) && it.num!! < (viewModel.getLast().num!!)) { //if this is true then we need to get again
                    viewModel.updateNext()
                }//else {
                (viewPager.adapter as ComicPagerAdapter).addComic(it)
                (viewPager.adapter as ComicPagerAdapter).notifyDataSetChanged()
                //}
            }
        })


        viewModel.observePrevComic().observe(this, Observer {
            if(it != null) {
                if (viewPager.currentItem == 0 && it.num!! > (viewModel.getFirst().num!!)) {
                    viewModel.updatePrev()
                }//else {
                if ((viewPager.adapter as ComicPagerAdapter).addFirstComic(it)) {
                    viewPager.setCurrentItem(1)
                    (viewPager.adapter as ComicPagerAdapter).notifyDataSetChanged()
                }
                //}
            }
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                println("position: " + position)
                println("size: " + (pagerAdapter!!.getCount()-1))
                var pagerAdapter = (viewPager.adapter as ComicPagerAdapter)

                if(position >= pagerAdapter!!.getCount()-1){
                    //call getNextComics which will fetch from network and
                    //alert the observer in MainActivity to call addComic
                    //viewModel.getNextComics(comics[position].num!!,1)

                    //viewModel.getNextComics(pagerAdapter.getComic(position).num!!,1)
                    if(!inFavorites){
                        viewModel.getNext()
                    }

                }
                if(position==0){
                    //TODO
                    //viewModel.getPrevComic(comics[0].num!!)
                    if(!inFavorites){
                        viewModel.getPrev()
                    }
                    //viewPager.setCurrentItem(1)
                    //viewModel.getPrev()
                    //viewPager.setCurrentItem(1)

                }
            }
        })

        viewModel.refreshLastComic()
        val rnds = (1..2000).random()
        jumpToComic(rnds.toString())


    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

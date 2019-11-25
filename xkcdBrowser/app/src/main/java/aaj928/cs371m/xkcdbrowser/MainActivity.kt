package aaj928.cs371m.xkcdbrowser

import aaj928.cs371m.xkcdbrowser.api.Comic
import aaj928.cs371m.xkcdbrowser.ui.ComicPagerAdapter
import aaj928.cs371m.xkcdbrowser.ui.ComicViewModel
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
import android.view.KeyEvent
import androidx.core.content.ContextCompat.getSystemService
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ComponentActivity.ExtraData



class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ComicPagerAdapter
    private lateinit var viewModel: ComicViewModel

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
                editText.isCursorVisible = false
                hideKeyboard()
                return false
            }
        })


        actionBar.customView = customView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
            initActionBar(it)
        }


        viewModel = ViewModelProviders.of(this)[ComicViewModel::class.java]
        viewModel.getNextComics(1, 1)
        viewModel.observeComics().observe(this, Observer {
            pagerAdapter = ComicPagerAdapter(supportFragmentManager, it, viewModel)
            viewPager = findViewById(R.id.viewPager)
            if(viewPager.adapter == null){
                viewPager.adapter = pagerAdapter
            }
            else{
                for(c : Comic in it){
                    (viewPager.adapter as ComicPagerAdapter).addComic(c)
                }
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

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

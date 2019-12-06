package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.MainActivity
import aaj928.cs371m.xkcdbrowser.R
import aaj928.cs371m.xkcdbrowser.api.Comic
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*


class FavoritesFragment: Fragment() {
    private lateinit var viewModel: ComicViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter : ComicPagerAdapter

    private lateinit var adapter : ComicPagerAdapter
    private var rv: RecyclerView? = null


    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    /*
    private fun titleSearch() {
        var search = (activity as MainActivity).supportActionBar!!.customView.findViewById<EditText>(R.id.actionSearch)
        search.addTextChangedListener{
            val s : String = it.toString()
            val filteredList = arrayListOf<RedditPost>()
            for(post in viewModel.observeFavoritesAsPosts()){
                if(post.title.toLowerCase().contains(s.toLowerCase())){
                    filteredList.add(post)
                }
            }
            adapter.submitList(filteredList)
            adapter.notifyDataSetChanged()
        }
    }*/


    /*
    private fun initRecyclerView(root : View) : PostRowListAdapter{
        rv = root.findViewById<RecyclerView>(R.id.searchResults)
        val adapter = PostRowListAdapter(viewModel, true)
        rv?.swapAdapter(adapter, false)
        //rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(context)
        return adapter
    }*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.activity_main, container, false)

        viewModel = ViewModelProviders.of(this)[ComicViewModel::class.java]
        viewPager = root.findViewById(R.id.viewPager)

        /*viewModel.observeComics().observe(this, Observer {
            if(viewPager.adapter == null){
                pagerAdapter = ComicPagerAdapter(fragmentManager!!/*, it*/, viewModel)
                for(c : Comic in it){
                    pagerAdapter.addComic(c)
                }
                viewPager.adapter = pagerAdapter

                if(it.first == it.last){
                    viewModel.getPrev()
                    viewModel.getNext()
                }
            }
        })*/

        /*val root =  inflater.inflate(R.layout.fragment_home, container, false)

        var action = (activity as MainActivity).supportActionBar!!.customView.findViewById<TextView>(R.id.actionTitle)
        action.text = "Favorites"

        viewModel = ViewModelProviders.of(this.activity!!)[MainViewModel::class.java]
        adapter = initRecyclerView(root)

        adapter.submitList(viewModel.observeFavoritesAsPosts())
        //titleSearch()
        return root*/
        return root
    }




}
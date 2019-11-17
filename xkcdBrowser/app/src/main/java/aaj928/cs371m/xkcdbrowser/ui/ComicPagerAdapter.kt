package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.api.Comic
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class ComicPagerAdapter(fragmentManager: FragmentManager, private val movies: List<Comic>) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return ComicFragment.newInstance(movies[position])
    }


    override fun getCount(): Int {
        return movies.size
    }
}
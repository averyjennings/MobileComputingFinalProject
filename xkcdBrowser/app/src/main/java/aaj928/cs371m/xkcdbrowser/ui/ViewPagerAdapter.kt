package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.api.Comic
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter


class ViewPagerAdapter(fragmentManager: FragmentManager, private var comics: ArrayList<Comic>, viewModel : ComicViewModel) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var viewModel = viewModel

    override fun getItem(position: Int): Fragment {
        if(position == comics.size-1){
            //call getNextComics which will fetch from network and
            //alert the observer in MainActivity to call addComic
            viewModel.getNextComics(position+1,1)
        }

        return ComicFragment.newInstance(comics[position])
    }

    fun addComic(c : Comic){
        if(!comics.contains(c)){
            comics.add(c)
            notifyDataSetChanged()
        }
    }


    override fun getCount(): Int {
        return comics.size
    }

    /*override fun getItemPosition(`object`: Any): Int {
         return super.getItemPosition(`object`)
         //return PagerAdapter.POSITION_NONE
     }*/

}
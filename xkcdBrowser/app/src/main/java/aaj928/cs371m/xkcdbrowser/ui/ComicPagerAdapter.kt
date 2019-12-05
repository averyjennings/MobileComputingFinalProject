package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.MainActivity
import aaj928.cs371m.xkcdbrowser.R
import aaj928.cs371m.xkcdbrowser.api.Comic
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_comic.*
import okhttp3.internal.notify
import java.util.*


class ComicPagerAdapter(fragmentManager: FragmentManager,  viewModel : ComicViewModel) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var viewModel = viewModel
    private var comics: LinkedList<ComicFragment> = LinkedList(listOf<ComicFragment>())
    private var positionOffset = 0
    private var size = 0
    //private var fragments : LinkedList<ComicFragment> =

    private var numSet = mutableSetOf<Int>()

    override fun getItem(position: Int): Fragment {
        /*
        println("position: " + position)

        val adjustedPos = position + positionOffset

        println("adjustedPos: " + adjustedPos)


        if(position >= comics.size-1){
            //call getNextComics which will fetch from network and
            //alert the observer in MainActivity to call addComic
            //val num : Int = comics[position].comic_num.text.toString().toInt()

            //viewModel.getNextComics(position+1,1)
            //viewModel.getNext()
        }
        if(adjustedPos==0){
            //TODO
            //viewModel.getPrevComic(comics[0].num!!)
            viewModel.getPrev()
        }

        //return ComicFragment.newInstance(comics[position])
        */
        return comics[position]
    }

    /*public fun getComic(position : Int) : Comic{
        return comics[position]
    }*/



    fun addComic(c : Comic){

        /*if(!comics.contains(c) /*&& !numSet.contains(c.num)*/ && c.num != comics[0].num){
            if(c.num!! < comics[0].num!!){
                //comics.add(c)
                //notifyDataSetChanged()
                //comics.removeLast()
                comics.push(c)

            }else {
                comics.add(c)
            }
            //numSet.add(c.num)

            notifyDataSetChanged()
        }*/
        if(!numSet.contains(c.num)){
            var frag = ComicFragment.newInstance(c)
            if(!comics.contains(frag)){
                comics.add(frag)
                size++
                notifyDataSetChanged()
            }
            numSet.add(c.num!!)
        }

    }

    fun addFirstComic(c : Comic) : Boolean{
        if(!numSet.contains(c.num)){
            var frag = ComicFragment.newInstance(c)
            if(!comics.contains(frag)){
                comics.addFirst(frag)
                size++
                notifyDataSetChanged()
                //positionOffset++

            }
            numSet.add(c.num!!)
            return true
        }
        return false
    }

    override fun getCount(): Int {
        return comics.size
        //return size
    }

   override fun getItemPosition(`object`: Any): Int {
        //return super.getItemPosition(`object`)
        return POSITION_NONE
       //return comics.indexOf(`object`)
    }

}
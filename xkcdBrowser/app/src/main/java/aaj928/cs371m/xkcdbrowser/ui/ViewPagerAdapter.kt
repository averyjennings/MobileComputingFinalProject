package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.api.Comic
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*
import android.R
import android.R.attr.button
import android.widget.TextView
import android.view.LayoutInflater




class ViewPagerAdapter(context : Context, private var comics: LinkedList<Comic>, viewModel : ComicViewModel) :
    PagerAdapter() {

    private var viewModel = viewModel
    private var context = context

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return comics.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        /*val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_item1, container, false) as ViewGroup

        val textView = view.findViewById(R.id.textView)

        val button = view.findViewById(R.id.button)
        button.setText(mListData.get(position))
        button.setOnClickListener(View.OnClickListener { textView.setText(mListData.get(position)) })
        */
        val butt = 0
        return 0

    }

    /*fun addComic(c : Comic){
        if(!comics.contains(c)){
            comics.add(c)
            notifyDataSetChanged()
        }
    }*/




    /*override fun getItemPosition(`object`: Any): Int {
         return super.getItemPosition(`object`)
         //return PagerAdapter.POSITION_NONE
     }*/

}
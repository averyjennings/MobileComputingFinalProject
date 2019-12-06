package aaj928.cs371m.xkcdbrowser.ui

import aaj928.cs371m.xkcdbrowser.MainActivity
import aaj928.cs371m.xkcdbrowser.R
import aaj928.cs371m.xkcdbrowser.api.Comic
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import aaj928.cs371m.xkcdbrowser.glide.Glide
import android.app.Activity
import androidx.appcompat.widget.AppCompatImageView
import android.content.DialogInterface
import android.widget.EditText
import android.content.Context
import androidx.appcompat.app.AlertDialog


class ComicFragment : Fragment() {

    private lateinit var viewModel : ComicViewModel
  //  private lateinit var num : Int



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

        // Creates the view controlled by the fragment
        val view = inflater.inflate(R.layout.fragment_comic, container, false)
        //TODO set all the things

        val heart = view.findViewById<ImageView>(R.id.fav)
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val theImageView = view.findViewById<ImageView>(R.id.image)
        val numView = view.findViewById<TextView>(R.id.comic_num)

        val args = arguments
        titleTextView.text = args?.getString("safe_title")
        val imageUrl = args?.getString("img")
        Glide.glideFetch(imageUrl!!, imageUrl!!, theImageView)

/*        theImageView.setOnTouchListener{
            v, event ->
            var x

            true
        }*/

     //  num = args?.getString("num").toInt()
        val num = args?.getString("num").toInt()
        numView.text = args?.getString("num")



        if(viewModel.isFavorited(num)){
            heart.setImageResource(R.drawable.ic_favorite_black_24dp)
        }else{
            heart.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }



        heart.setOnClickListener{
            if(viewModel.isFavorited(num)){
                viewModel.removeFavorite(num)
                heart.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                /*if(unfavoriteIsRemove) {
                    submitList(viewModel.observeFavoritesAsPosts())
                    notifyDataSetChanged()
                }*/
            }else{
                viewModel.addFavorite(num)
                heart.setImageResource(R.drawable.ic_favorite_black_24dp)
            }
        }

        theImageView.setOnLongClickListener {
            it -> true
            //TODO display alt text
            showAddItemDialog(this.activity!!, args?.getString("alt"))
            true
        }




        return view
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.activity!!)[ComicViewModel::class.java]

    }

    private fun showAddItemDialog(c: Context, s : String) {
        val taskTextView = TextView(c)
        val dialog = AlertDialog.Builder(c)
            .setMessage(s)
            .setView(taskTextView)
            .create()
        dialog.show()
    }


    companion object {

        // Method for creating new instances of the fragment
        fun newInstance(comic: Comic): ComicFragment {

            // Store the movie data in a Bundle object
            val args = Bundle()
            args.putString("month", comic.month)
            args.putString("num", comic.num.toString())
            args.putString("link", comic.link)
            args.putString("year", comic.year)
            args.putString("news", comic.news)
            args.putString("safe_title", comic.safeTitle)
            args.putString("transcript", comic.transcript)
            args.putString("alt", comic.alt)
            args.putString("img", comic.imageUrl)
            args.putString("title", comic.title)
            args.putString("day", comic.day)



            // Create a new MovieFragment and set the Bundle as the arguments
            // to be retrieved and displayed when the view is created
            val fragment = ComicFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

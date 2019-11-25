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
import androidx.appcompat.widget.AppCompatImageView

class ComicFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

        // Creates the view controlled by the fragment
        val view = inflater.inflate(R.layout.fragment_comic, container, false)
        //TODO set all the things

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val theImageView = view.findViewById<ImageView>(R.id.image)
        val numView = view.findViewById<TextView>(R.id.comic_num)

        val args = arguments
        titleTextView.text = args?.getString("safe_title")
        val imageUrl = args?.getString("img")
        Glide.glideFetch(imageUrl!!, imageUrl!!, theImageView)

        theImageView.setOnLongClickListener {
            it -> true
            //TODO display alt text
        }

        numView.text = args?.getString("num")





        return view
    }

    fun onCreate(){

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
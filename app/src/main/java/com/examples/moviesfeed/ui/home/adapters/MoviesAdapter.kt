package com.examples.moviesfeed.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.examples.moviesfeed.R
import com.examples.moviesfeed.model.Movie
import java.lang.NullPointerException

class MoviesAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_list_item_vertical, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val title: TextView = itemView.findViewById(R.id.item_movie_title)
        private val overview: TextView = itemView.findViewById(R.id.item_movie_overview)

        fun bind(movie: Movie) {
            try {
                val requestBackgroundOptions: RequestOptions =
                    RequestOptions().transforms(CenterCrop(), RoundedCorners(20))
                Glide.with(itemView)
                    .load(movie.multimedia.posterPath)
                    .transform(CenterCrop())
                    .apply(requestBackgroundOptions)
                    .into(poster)

            } catch (e: NullPointerException) {
                poster.setImageResource(R.drawable.no_image_avl)
            }
            title.text = movie.title

            val stringLimit = 250
            val overviewString: String = movie.overview

            if (overviewString.length < stringLimit) {
                overview.text = overviewString
            } else {
                overview.text = overviewString.substring(0, stringLimit) + "..."
            }
        }
    }
}
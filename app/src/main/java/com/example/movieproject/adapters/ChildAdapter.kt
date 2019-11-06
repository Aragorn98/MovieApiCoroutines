package com.example.movieproject.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.movieproject.R
import com.example.movieproject.api.models.Movie
import com.example.movieproject.listeners.MovieClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.child_recycler.view.*

class ChildAdapter(private val children : List<Movie>)
    : RecyclerView.Adapter<ChildAdapter.ViewHolder>(){
    private lateinit var movieClickListener: MovieClickListener

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.child_recycler,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    fun setListener(listener: MovieClickListener) {
        movieClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = children[position]
//        holder.imageView.setImageResource(child.image)
//        holder.imageView.setImageResource(R.drawable.aviator)
        Picasso.get().load("http://image.tmdb.org/t/p/w780" + child.poster_path).into(holder.imageView)
        holder.textView.text = child.title
        holder.bindQuote(child)
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textView : TextView = itemView.child_textView
        val imageView: ImageView = itemView.child_imageView

        fun bindQuote(movie: Movie) {
            with(itemView) {
                setOnClickListener {
                    movieClickListener.onMovieClicked(movie)
                }
            }
        }

    }
}
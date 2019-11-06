package com.example.movieproject.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieproject.R
import com.example.movieproject.api.models.Cast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_credit.view.*

class CreditsAdapter(private val casts: ArrayList<Cast> = ArrayList())
    : RecyclerView.Adapter<CreditsAdapter.CastViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_credit, parent, false))

    override fun getItemCount() = casts.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bindQuote(casts[position])
    }

    fun setCasts(data: List<Cast>) {
        //movies.clear()
        casts.addAll(data)

        notifyDataSetChanged()
    }


    inner class CastViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

        fun bindQuote(cast: Cast) {
            with(root) {
                name.text = cast.name
                character.text = cast.character

                Picasso.get().load("http://image.tmdb.org/t/p/w780" + cast.profile_path).into(credit_imageView)

            }
        }

    }
}
package com.example.movieproject.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieproject.R
import com.example.movieproject.api.models.ProductionCompany
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_logo.view.*

class LogosAdapter(private val productionCompanies: ArrayList<ProductionCompany> = ArrayList())
    : RecyclerView.Adapter<LogosAdapter.LogoViewHolder>() {
    override fun getItemCount() = productionCompanies.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LogoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_logo, parent, false))


    override fun onBindViewHolder(holder: LogoViewHolder, position: Int) {
        holder.bindQuote(productionCompanies[position])
    }

    fun setLogos(data: List<ProductionCompany>) {
//        Log.d("argyn", data[0].logo_path)
        //movies.clear()
        productionCompanies.addAll(data)

        notifyDataSetChanged()
    }


    inner class LogoViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

        fun bindQuote(productionCompany: ProductionCompany) {
            with(root) {
                Picasso.get().load("http://image.tmdb.org/t/p/w780" + productionCompany.logo_path).into(logo_imageView)
            }
        }

    }
}
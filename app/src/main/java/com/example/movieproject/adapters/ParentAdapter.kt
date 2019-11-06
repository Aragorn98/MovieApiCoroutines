package com.example.movieproject.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.movieproject.R
import com.example.movieproject.api.models.Result
import com.example.movieproject.listeners.MovieClickListener
import kotlinx.android.synthetic.main.parent_recycler.view.*

class ParentAdapter(private val parents : List<Result>,
                    private val onMoreClickedListener: OnMoreClickedListener,
                    private val onMovieClickListener: MovieClickListener
) :    RecyclerView.Adapter<ParentAdapter.ViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_recycler,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parents[position]
        Log.d("argyn", parents.toString())

        holder.textView.text = parent.type
        val childLayoutManager = LinearLayoutManager(
            holder.recyclerView.context, LinearLayout.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount
        var childAdapter = ChildAdapter(parent.results)
        childAdapter.setListener(onMovieClickListener)
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = childAdapter
            setRecycledViewPool(viewPool)
        }

        holder.moreButton.setOnClickListener {
            onMoreClickedListener.onMoreClicked(parent)
        }


    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_child
        val textView: TextView = itemView.textView
        val moreButton: Button = itemView.more
    }
}

interface OnMoreClickedListener {
    fun onMoreClicked(result: Result)
}
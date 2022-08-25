package com.aplikasi.capstone.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.capstone.ResultItem
import com.aplikasi.capstone.databinding.ItemReccomendationTourismBinding
import com.bumptech.glide.Glide


class ListRecommendation: RecyclerView.Adapter<ListRecommendation.ListViewHolder>() {

    private val listTourism = ArrayList<ResultItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataUser(user: List<ResultItem>) {
        listTourism.clear()
        listTourism.addAll(user)
        notifyDataSetChanged()
        Log.d("TAG", "cek $listTourism")
    }

    class ListViewHolder(val binding: ItemReccomendationTourismBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemReccomendationTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listTourism[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(user.thumbnail) // URL Gambar
                .into(imgPlace) // imageView mana yang akan diterapkan
            nameLocation.text = user.name
            addressLocation.text = user.city
            ratting.text = user.rating.toDouble().toString()
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listTourism[position]) }

    }

    override fun getItemCount(): Int = listTourism.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultItem)
    }
}


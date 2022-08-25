package com.aplikasi.capstone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.capstone.ResultItem
import com.aplikasi.capstone.databinding.ListTourismBinding
import com.bumptech.glide.Glide

class ListTourismAdapter(private val listTourism: List<ResultItem>) :
    RecyclerView.Adapter<ListTourismAdapter.ViewHolder>() {

    class ViewHolder(var binding: ListTourismBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(tourism: ResultItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(tourism.thumbnail)
                    .into(image)
            }

        }
    }


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(accountClicked: ResultItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(listTourism[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listTourism[position]) }
    }


    override fun getItemCount(): Int = listTourism.size
}
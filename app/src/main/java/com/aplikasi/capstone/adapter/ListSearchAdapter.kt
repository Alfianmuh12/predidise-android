package com.aplikasi.capstone.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.capstone.ResultItem
import com.aplikasi.capstone.databinding.ListSearchBinding
import com.bumptech.glide.Glide

class ListSearchAdapter: RecyclerView.Adapter<ListSearchAdapter.ListViewHolder>() {

    private val listUsers = ArrayList<ResultItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataUser(user: List<ResultItem>) {
        listUsers.clear()
        listUsers.addAll(user)
        notifyDataSetChanged()
        Log.d("TAG", "cek $listUsers")
    }

    class ListViewHolder(val binding: ListSearchBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUsers[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(user.thumbnail) // URL Gambar
                .circleCrop() // Mengubah image menjadi lingkaran
                .into(imgAvatar) // imageView mana yang akan diterapkan
            tvUsername.text = user.name
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[position]) }

    }

    override fun getItemCount(): Int = listUsers.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultItem)
    }
}




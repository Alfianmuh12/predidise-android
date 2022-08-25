package com.aplikasi.capstone.favorite

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.capstone.databinding.ListFavoriteBinding
import com.bumptech.glide.Glide


class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>(){

    private val listUsers = ArrayList<Favorite>()

    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataUser(user: ArrayList<Favorite>){
        listUsers.clear()
        listUsers.addAll(user)
        notifyDataSetChanged()
        Log.d("TAG", "cek $listUsers")
    }

    class ListViewHolder(val binding: ListFavoriteBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUsers[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(user.avatar_url) // URL Gambar // Mengubah image menjadi lingkaran
                .into(imgPlace) // imageView mana yang akan diterapkan
            tvUsername.text = user.username
            tvLocation.text = user.location
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[position]) }

    }

    override fun getItemCount(): Int = listUsers.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }




}
package com.aplikasi.capstone.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.capstone.ResultItem
import com.aplikasi.capstone.databinding.ItemReccomendationTourismBinding
import com.aplikasi.capstone.databinding.ListReviewBinding
import com.aplikasi.capstone.response.Review
import com.bumptech.glide.Glide


class ListReview: RecyclerView.Adapter<ListReview.ListViewHolder>() {

    private val listReview = ArrayList<Review>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataUser(user: List<Review>) {
        listReview.clear()
        listReview.addAll(user)
        notifyDataSetChanged()
        Log.d("TAG", "cek $listReview")
    }

    class ListViewHolder(val binding: ListReviewBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listReview[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load("http://34.126.178.52/uploads/1K4TTVQ7zvoSHBOBWYyFH0tGTZl0SBwlMszJRRe7.jpg") // URL Gambar
                .into(imgUser)

            Glide.with(root.context)
                .load("http://34.126.178.52/uploads/sdGzuCnyMOrc3WeskIndV6MSIjOD5OALTL8EandN.jpg") // URL Gambar
                .into(ivReview)// imageView mana yang akan diterapkan
            tvUsername.text = user.name
            tvDescription.text = user.review
            ratingBar.rating = user.rating.toFloat()

        }

    }

    override fun getItemCount(): Int = listReview.size

}


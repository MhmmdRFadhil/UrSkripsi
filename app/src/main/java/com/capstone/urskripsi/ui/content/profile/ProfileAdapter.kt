package com.capstone.urskripsi.ui.content.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.databinding.ItemRowProfileBinding

class ProfileAdapter(private val profile: ArrayList<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRowProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) {
            with(binding) {
                profile.image?.let { itemImage.setImageResource(it) }
                itemTv.text = profile.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profile[position])
    }

    override fun getItemCount(): Int = profile.size
}
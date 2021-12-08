package com.capstone.urskripsi.ui.content.profile.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.databinding.ItemRowDeveloperBinding
import com.capstone.urskripsi.utils.Utility.loadImageUrl

class DeveloperAdapter(private val developer: ArrayList<Developer>) :
    RecyclerView.Adapter<DeveloperAdapter.ViewHolder>() {

    private lateinit var onItemClickedCallback: OnItemClickedCallback

    fun setOnItemClickedCallback(onItemClickedCallback: OnItemClickedCallback) {
        this.onItemClickedCallback = onItemClickedCallback
    }

    inner class ViewHolder(private val binding: ItemRowDeveloperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(developer: Developer) {
            with(binding) {
                developerImg.loadImageUrl(developer.image.toString())
                tvDeveloperName.text = developer.name
                tvUniversityNameDeveloper.text = developer.universityName
            }
            itemView.setOnClickListener {
                onItemClickedCallback.onItemClick(developer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemRowDeveloperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(developer[position])
    }

    override fun getItemCount(): Int = developer.size

    interface OnItemClickedCallback {
        fun onItemClick(developer: Developer)
    }
}
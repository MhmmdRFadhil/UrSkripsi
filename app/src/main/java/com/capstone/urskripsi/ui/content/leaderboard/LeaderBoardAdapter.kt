package com.capstone.urskripsi.ui.content.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.databinding.ItemRowLeaderboardBinding
import com.capstone.urskripsi.utils.Utility.loadImageUrl

class LeaderBoardAdapter(private val leaderBoard: ArrayList<LeaderBoard>) :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRowLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leaderBoard: LeaderBoard) {
            with(binding) {
                imgProfile.loadImageUrl(leaderBoard.image.toString())
                tvNama.text = leaderBoard.name
                tvProgress.text = leaderBoard.progressBar
                progressIndicatorBar.progress = leaderBoard.progressBar?.toInt() as Int
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemRowLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(leaderBoard[position])
    }

    override fun getItemCount(): Int = leaderBoard.size
}
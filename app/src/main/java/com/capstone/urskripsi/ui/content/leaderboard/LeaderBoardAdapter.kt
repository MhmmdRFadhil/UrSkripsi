package com.capstone.urskripsi.ui.content.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ItemRowLeaderboardBinding
import com.capstone.urskripsi.utils.Utility.loadImageUrl

class LeaderBoardAdapter(private val leaderBoard: ArrayList<LeaderBoard>) :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRowLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leaderBoard: LeaderBoard) {
            with(binding) {
                if (leaderBoard.image != null) {
                    imgProfile.loadImageUrl(leaderBoard.image.toString())
                } else {
                    imgProfile.setImageResource(R.drawable.avatar)
                }

                tvNama.text = leaderBoard.name
                tvProgress.text = leaderBoard.progressBar.toString()
                progressIndicatorBar.progress = leaderBoard.progressBar as Int

                if (leaderBoard.progressBar == 100) {
                    imgBadge.setImageResource(R.drawable.ic_badge_completed)
                } else {
                    imgBadge.setImageResource(R.drawable.ic_badge)
                }
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
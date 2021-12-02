package com.capstone.urskripsi.ui.content.leaderboard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentLeaderBoardBinding
import com.capstone.urskripsi.utils.Utility.setTitleColor

class LeaderBoardFragment : Fragment() {

    private var binding: FragmentLeaderBoardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.apply {
                title = getString(R.string.leaderboard)
                setTitleColor(Color.BLACK)
                setBackgroundDrawable(ColorDrawable(Color.parseColor(WHITE)))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val WHITE = "#FFFFFF"
    }
}
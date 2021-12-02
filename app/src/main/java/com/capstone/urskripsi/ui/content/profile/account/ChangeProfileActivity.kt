package com.capstone.urskripsi.ui.content.profile.account

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityChangeProfileBinding
import com.capstone.urskripsi.utils.Utility.setTitleColor

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.change_profile)
            setTitleColor(Color.BLACK)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            setBackgroundDrawable(ColorDrawable(Color.parseColor(WHITE)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val WHITE = "#FFFFFF"
    }
}
package com.capstone.urskripsi.ui.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityForgotPasswordBinding
import com.capstone.urskripsi.utils.Utility.setTitleColor
import com.capstone.urskripsi.utils.Utility.showToast

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.forgot_password)
            setTitleColor(Color.BLACK)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        }

        binding.apply {
            btnForgotPassword.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                var isEmptyField = false

                if (email.isEmpty()) {
                    isEmptyField = true
                }

                if (isEmptyField) {
                    showToast(getString(R.string.empty_email), this@ForgotPasswordActivity)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
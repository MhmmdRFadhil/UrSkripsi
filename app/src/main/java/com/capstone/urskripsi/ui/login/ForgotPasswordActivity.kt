package com.capstone.urskripsi.ui.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityForgotPasswordBinding
import com.capstone.urskripsi.utils.Utility.setTitleColor
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        supportActionBar?.apply {
            title = getString(R.string.forgot_password)
            setTitleColor(Color.BLACK)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            setBackgroundDrawable(ColorDrawable(Color.parseColor(WHITE)))
        }

        binding.apply {
            btnForgotPassword.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                var isEmptyField = false

                if (email.isEmpty()) {
                    isEmptyField = true
                    edtEmail.error = getString(R.string.email_empty)
                }

                if (!isEmptyField) {
                    setForgotPassword()
                }
            }

            tvBackToLogin.setOnClickListener {
                startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setForgotPassword() {
        val email = binding.edtEmail.text.toString().trim()

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {

            showToast(getString(R.string.reset_password), this@ForgotPasswordActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val WHITE = "#FFFFFF"
    }
}
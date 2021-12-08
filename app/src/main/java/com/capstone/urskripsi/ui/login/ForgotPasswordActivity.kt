package com.capstone.urskripsi.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityForgotPasswordBinding
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        simpleToolbar(getString(R.string.forgot_password), binding.toolbar.root, true)

        binding.apply {
            btnForgotPassword.setOnClickListener {
                val email = edtEmail.text.toString().trim()

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
                    edtEmail.error = resources.getString(R.string.invalid_formail_email)
                } else if (TextUtils.isEmpty(email)) {
                    edtEmail.error = resources.getString(R.string.email_empty)
                } else {
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
        binding.progressBarDialog.root.show()
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            binding.progressBarDialog.root.hide()
            showToast(getString(R.string.email_send_success), this@ForgotPasswordActivity)
        }.addOnFailureListener {
            binding.progressBarDialog.root.hide()
            showToast(getString(R.string.email_send_failed), this@ForgotPasswordActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
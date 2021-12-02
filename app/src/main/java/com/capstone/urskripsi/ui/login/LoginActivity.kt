package com.capstone.urskripsi.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityLoginBinding
import com.capstone.urskripsi.ui.login.fragment.SignInFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment()

        mAuth = FirebaseAuth.getInstance()
        checkUserIfAlreadyLogin()
    }

    private fun checkUserIfAlreadyLogin() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setFragment() {
        val mFragmentManager = supportFragmentManager
        val mLoginFragment = SignInFragment()
        val fragment = mFragmentManager.findFragmentByTag(mLoginFragment::class.java.simpleName)

        if (fragment !is SignInFragment) {
            Log.d(TAG, FRAGMENT_NAME + SignInFragment::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_layout, mLoginFragment, SignInFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    companion object {
        const val TAG = "LoginFragment"
        const val FRAGMENT_NAME = "Fragment Name: "
    }
}
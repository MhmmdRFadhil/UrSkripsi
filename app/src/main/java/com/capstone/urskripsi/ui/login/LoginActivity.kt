package com.capstone.urskripsi.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityLoginBinding
import com.capstone.urskripsi.ui.login.fragment.SignInFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment()
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


    companion object {
        const val TAG = "LoginFragment"
        const val FRAGMENT_NAME = "Fragment Name: "
    }
}
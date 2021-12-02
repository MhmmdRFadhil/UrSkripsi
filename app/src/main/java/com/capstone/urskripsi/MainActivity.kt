package com.capstone.urskripsi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.capstone.urskripsi.databinding.ActivityMainBinding
import com.capstone.urskripsi.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationController = Navigation.findNavController(this, R.id.navigationHost)

        ExpandableBottomBarNavigationUI.setupWithNavController(binding.bottomNavigation, navigationController)

        checkUserIfAlreadyLogin()
    }

    private fun checkUserIfAlreadyLogin() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
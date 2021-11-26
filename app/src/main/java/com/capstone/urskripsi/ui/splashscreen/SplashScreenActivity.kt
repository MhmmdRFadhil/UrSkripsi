package com.capstone.urskripsi.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivitySplashScreenBinding
import com.capstone.urskripsi.ui.onboarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnBoardingActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, TIMEOUT.toLong())

        // set animation text in splashscreen
        binding.tvTitleSplashScreen.apply {
            scaleX = 0f
            scaleY = 0f
            animate().let {
                it.duration = 600
                it.scaleXBy(1.0f)
                it.scaleYBy(1.0f)
            }
        }

        transparentStatusAndNavigation()
    }

    private fun transparentStatusAndNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val wic = WindowCompat.getInsetsController(window, window.decorView)
            wic?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE

            wic?.isAppearanceLightStatusBars = true
            wic?.isAppearanceLightNavigationBars = true
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT

        }
    }

    companion object {
        const val TIMEOUT = 4000
    }
}
package com.hiteshvirani.googlyplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val editor = getSharedPreferences("Themes", MODE_PRIVATE)
        setTheme(MainActivity.themeList[editor.getInt("themeIndex", 5)])
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        try {
            Thread {
                Thread.sleep(1325)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.start()
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}



//for hide status bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            window.attributes.layoutInDisplayCutoutMode =
//                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//        }
//        this.getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

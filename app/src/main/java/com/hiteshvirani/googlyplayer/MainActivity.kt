package com.hiteshvirani.googlyplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hiteshvirani.googlyplayer.databinding.ActivityMainBinding
import com.hiteshvirani.googlyplayer.databinding.ThemeViewBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var runnable: Runnable? = null

    companion object {
        lateinit var videoList: ArrayList<Video>
        lateinit var folderList: ArrayList<Folder>
        lateinit var searchList: ArrayList<Video>
        var search: Boolean = false
        var themeIndex: Int = 5
        var sortValue: Int = 0
        val themeList = arrayOf(
            R.style.theme_1,
            R.style.theme_2,
            R.style.theme_3,
            R.style.theme_4,
            R.style.theme_5,
            R.style.theme_6,
            R.style.theme_7,
            R.style.theme_8,
            R.style.theme_9
        )
        var dataChanged: Boolean = false
        var adapterChanged: Boolean = false
        val sortList = arrayOf(
            MediaStore.Video.Media.DATE_ADDED + " DESC", MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.TITLE + " DESC",
            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.SIZE + " DESC"
        )
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editor = getSharedPreferences("Themes", MODE_PRIVATE)
        themeIndex = editor.getInt("themeIndex", 5)

        setTheme(themeList[themeIndex])
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //for Navigation Drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (requestRuntimePermission()) {
            folderList = ArrayList()
            videoList = getAllVideo(this)
            setFragment(VideosFragment())

            if (dataChanged) {
                runnable = Runnable {
                    if (dataChanged) {
                        videoList = getAllVideo(this)
                        dataChanged = false
                        adapterChanged = true
                    }
                    Handler(Looper.getMainLooper()).postDelayed(runnable!!, 200)
                }
                Handler(Looper.getMainLooper()).postDelayed(runnable!!, 0)
            }
        }

        binding.idBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_all_videosNav -> setFragment(VideosFragment())
                R.id.id_all_foldersNav -> setFragment(FoldersFragment())
            }
            return@setOnItemSelectedListener true
        }
        binding.idNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.id_themesNav -> {
                    val customDialogT =
                        LayoutInflater.from(this).inflate(R.layout.theme_view, binding.root, false)
                    val bindingTheme = ThemeViewBinding.bind(customDialogT)
                    val dialog = MaterialAlertDialogBuilder(this, R.style.MyDialogTheme).setView(
                        customDialogT
                    )
                        .setTitle("Select Theme")
                        .create()
                    dialog.show()

                    when (themeIndex) {
                        0 -> bindingTheme.theme1.setBackgroundColor(Color.WHITE)
                        1 -> bindingTheme.theme2.setBackgroundColor(Color.WHITE)
                        2 -> bindingTheme.theme3.setBackgroundColor(Color.WHITE)
                        3 -> bindingTheme.theme4.setBackgroundColor(Color.WHITE)
                        4 -> bindingTheme.theme5.setBackgroundColor(Color.WHITE)
                        5 -> bindingTheme.theme6.setBackgroundColor(Color.WHITE)
                        6 -> bindingTheme.theme7.setBackgroundColor(Color.WHITE)
                        7 -> bindingTheme.theme8.setBackgroundColor(Color.WHITE)
                        8 -> bindingTheme.theme9.setBackgroundColor(Color.WHITE)
                    }

                    bindingTheme.theme1.setOnClickListener {
                        saveTheme(0)
                        dialog.dismiss()
                    }
                    bindingTheme.theme2.setOnClickListener {
                        saveTheme(1)
                        dialog.dismiss()
                    }
                    bindingTheme.theme3.setOnClickListener {
                        saveTheme(2)
                        dialog.dismiss()
                    }
                    bindingTheme.theme4.setOnClickListener {
                        saveTheme(3)
                        dialog.dismiss()
                    }
                    bindingTheme.theme5.setOnClickListener {
                        saveTheme(4)
                        dialog.dismiss()
                    }
                    bindingTheme.theme6.setOnClickListener {
                        saveTheme(5)
                        dialog.dismiss()
                    }
                    bindingTheme.theme7.setOnClickListener {
                        saveTheme(6)
                        dialog.dismiss()
                    }
                    bindingTheme.theme8.setOnClickListener {
                        saveTheme(7)
                        dialog.dismiss()
                    }
                    bindingTheme.theme9.setOnClickListener {
                        saveTheme(8)
                        dialog.dismiss()
                    }

                }
                R.id.id_sortOrderNav -> {
                    val menuList = arrayOf(
                        "Latest",
                        "Oldest",
                        "Name(A to Z)",
                        "Name(Z to A)",
                        "File Size(Smallest)",
                        "File Size(Largest)"
                    )
                    var value = sortValue
                    val dialog = MaterialAlertDialogBuilder(this, R.style.MyDialogTheme2)
                        .setTitle("Sort By")
                        .setPositiveButton("OK") { _, _ ->
                            val sortEditor = getSharedPreferences("Sorting", MODE_PRIVATE).edit()
                            sortEditor.putInt("sortValue", value)
                            sortEditor.apply()

                            finish()
                            startActivity(intent)
                        }
                        .setSingleChoiceItems(menuList, sortValue) { _, pos ->
                            value = pos
                        }
                        .create()
                    dialog.show()
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                        MaterialColors.getColor(this, R.attr.themeColor, Color.WHITE)
//                    )
                }
                R.id.id_aboutNav -> startActivity(Intent(this, AboutActivity::class.java))
                R.id.id_exitNav -> {
                    exitProcess(1)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    //for requesting permission
    private fun requestRuntimePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 29)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 29) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                folderList = ArrayList()
                videoList = getAllVideo(this)
                setFragment(VideosFragment())
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 29)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveTheme(index: Int) {
        val editor = getSharedPreferences("Themes", MODE_PRIVATE).edit()
        editor.putInt("themeIndex", index)
        editor.apply()
        this.recreate()

    }

    override fun onDestroy() {
        super.onDestroy()
        runnable = null
    }

}
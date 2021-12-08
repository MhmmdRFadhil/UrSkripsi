package com.capstone.urskripsi.ui.content.profile.about

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityDeveloperBinding
import com.capstone.urskripsi.utils.Utility.loadImageUrl
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog

class DeveloperActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeveloperBinding
    private lateinit var developer: ArrayList<Developer>
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleToolbar(getString(R.string.developer), binding.toolbar.root, true)
        bottomSheetDialog = BottomSheetDialog(this)

        dataDeveloper()
        showRecyclerView()
    }

    private fun dataDeveloper() {
        developer = ArrayList()
        developer.apply {
            add(
                Developer(
                    getString(R.string.developer_image_1),
                    getString(R.string.developer_name_1),
                    getString(R.string.university_name_developer_1),
                    getString(R.string.github_developer_1),
                    getString(R.string.instagram_developer_1),
                    getString(R.string.linkedin_developer_1)
                )
            )
            add(
                Developer(
                    getString(R.string.developer_image_2),
                    getString(R.string.developer_name_2),
                    getString(R.string.university_name_developer_2),
                    getString(R.string.github_developer_2),
                    getString(R.string.instagram_developer_2),
                    getString(R.string.linkedin_developer_2)
                )
            )
        }
    }

    private fun showRecyclerView() {
        with(binding) {
            rvDeveloper.setHasFixedSize(true)
            rvDeveloper.layoutManager = LinearLayoutManager(this@DeveloperActivity)
            val developerAdapter = DeveloperAdapter(developer)
            rvDeveloper.adapter = developerAdapter
            developerAdapter.setOnItemClickedCallback(object :
                DeveloperAdapter.OnItemClickedCallback {
                override fun onItemClick(developer: Developer) {
                    bottomSheetDialogDetail(developer)
                }
            })
        }
    }

    private fun bottomSheetDialogDetail(developer: Developer) {
        val view = View.inflate(this, R.layout.bottom_sheet_dialog_developer, null)

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        val developerImage = bottomSheetDialog.findViewById<ImageView>(R.id.developer_img)
        val developerName = bottomSheetDialog.findViewById<TextView>(R.id.tv_developer_name)
        val universityNameDeveloper =
            bottomSheetDialog.findViewById<TextView>(R.id.tv_university_name_developer)
        val imgGithub = bottomSheetDialog.findViewById<ImageView>(R.id.imgGithub)
        val imgLinkedIn = bottomSheetDialog.findViewById<ImageView>(R.id.imgLinkedIn)
        val imgInstagram = bottomSheetDialog.findViewById<ImageView>(R.id.imgInstagram)

        developer.apply {
            developerImage?.loadImageUrl(image.toString())
            developerName?.text = name.toString()
            universityNameDeveloper?.text = universityName.toString()
            imgGithub?.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.github)))
            }
            imgLinkedIn?.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.linkedIn)))
            }
            imgInstagram?.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.instagram)))
            }
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
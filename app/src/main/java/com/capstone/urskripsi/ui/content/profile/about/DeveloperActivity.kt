package com.capstone.urskripsi.ui.content.profile.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityDeveloperBinding
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
                    val developerBottomSheetFragment = DeveloperBottomSheetFragment(developer)
                    developerBottomSheetFragment.show(supportFragmentManager, "TAG")
                }
            })
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
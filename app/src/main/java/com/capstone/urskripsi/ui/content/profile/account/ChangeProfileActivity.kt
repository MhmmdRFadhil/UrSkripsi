package com.capstone.urskripsi.ui.content.profile.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityChangeProfileBinding
import com.capstone.urskripsi.utils.Utility.setStateColor
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleToolbar(getString(R.string.change_profile), binding.toolbar.root, true)

        checkInput()
    }

    private fun checkInput() {
        binding.apply {
            edtFullName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.primaryColor,
                            true
                        )
                    } else {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.button_disable,
                            false
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            edtEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.primaryColor,
                            true
                        )
                    } else {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.button_disable,
                            false
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            edtUniversityName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.primaryColor,
                            true
                        )
                    } else {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.button_disable,
                            false
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            edtStudyProgram.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.primaryColor,
                            true
                        )
                    } else {
                        toolbar.btnSave.setStateColor(
                            this@ChangeProfileActivity,
                            R.color.button_disable,
                            false
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            toolbar.btnSave.setOnClickListener {
                showToast(getString(R.string.save_success), this@ChangeProfileActivity)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.capstone.urskripsi.ui.content.profile.account

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityChangeProfileBinding
import com.capstone.urskripsi.utils.FirebaseKey.Companion.FIREBASE_EMAIL
import com.capstone.urskripsi.utils.FirebaseKey.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.FirebaseKey.Companion.FIREBASE_STUDY_PROGRAM
import com.capstone.urskripsi.utils.FirebaseKey.Companion.FIREBASE_UNIVERSITY_NAME
import com.capstone.urskripsi.utils.FirebaseKey.Companion.FIREBASE_USERNAME
import com.capstone.urskripsi.utils.Utility.loadImageURI
import com.capstone.urskripsi.utils.Utility.loadImageUrl
import com.capstone.urskripsi.utils.Utility.setStateColor
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        simpleToolbar(getString(R.string.change_profile), binding.toolbar.root, true)

        retrieveData()
        selectPhoto()
    }

    private val resultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.apply {
                    imgProfile.loadImageURI(it)
                    imgUri = it
                    toolbar.btnSave.setStateColor(
                        this@ChangeProfileActivity,
                        R.color.primaryColor,
                        true
                    )
                }
            }
        }

    // Upload Photo to Firebase
    private fun uploadPhoto(uri: Uri) {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        storageReference = FirebaseStorage.getInstance().getReference("User/$setEmail/Data")

        val fileRef: StorageReference = storageReference.child(
            System.currentTimeMillis().toString() + "." + getFileExtension(uri)
        )

        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                val map = HashMap<String, Any>()
                map[FIREBASE_PHOTO] = it.toString()
                databaseReference.updateChildren(map)
                showToast(getString(R.string.upload_photo_success), this@ChangeProfileActivity)
            }
        }
    }

    // Set Format File Photo
    private fun getFileExtension(uri: Uri): String? {
        val cr: ContentResolver = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
    // End

    // Retrieve Data from Firebase
    private fun retrieveData() {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val photo = snapshot.child(FIREBASE_PHOTO).value
                    val name = snapshot.child(FIREBASE_USERNAME).value
                    val email = snapshot.child(FIREBASE_EMAIL).value
                    val universityName = snapshot.child(FIREBASE_UNIVERSITY_NAME).value
                    val studyProgram = snapshot.child(FIREBASE_STUDY_PROGRAM).value
                    binding.apply {
                        imgProfile.loadImageUrl(photo.toString())
                        edtName.setText(name.toString())
                        edtEmail.setText(email.toString())
                        if (universityName != null && studyProgram != null) {
                            edtUniversityName.setText(universityName.toString())
                            edtStudyProgram.setText(studyProgram.toString())
                        }
                        checkInput(
                            name.toString(),
                            email.toString(),
                            universityName.toString(),
                            studyProgram.toString()
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    // End

    // Set State Button Enable or Disable
    private fun checkInput(
        name: String?,
        email: String?,
        universityName: String?,
        studyProgram: String?
    ) {
        binding.apply {
            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty() && p0.toString() != name) {
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
                    if (p0.toString().isNotEmpty() && p0.toString() != email) {
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
                    if (p0.toString().isNotEmpty() && p0.toString() != universityName) {
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
                    if (p0.toString().isNotEmpty() && p0.toString() != studyProgram) {
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
        }
        saveDataUser()
    }
    // End

    // Save Data User to Firebase
    private fun saveDataUser() {
        binding.apply {
            toolbar.btnSave.setOnClickListener {
                val map = HashMap<String, Any>()
                imgUri?.let { uri ->
                    uploadPhoto(uri)
                }

                if (edtName.text.toString().trim().isNotEmpty()) {
                    map[FIREBASE_USERNAME] = edtName.text.toString().trim()
                }

                if (edtEmail.text.toString().trim().isNotEmpty()) {
                    map[FIREBASE_EMAIL] = edtEmail.text.toString().trim()
                }

                if (edtUniversityName.text.toString().trim().isNotEmpty()) {
                    map[FIREBASE_UNIVERSITY_NAME] =
                        edtUniversityName.text.toString().trim()
                }

                if (edtStudyProgram.text.toString().trim().isNotEmpty()) {
                    map[FIREBASE_STUDY_PROGRAM] = edtStudyProgram.text.toString().trim()
                }
                databaseReference.updateChildren(map)
                showToast(getString(R.string.save_success), this@ChangeProfileActivity)
            }
        }
    }
    // End

    // Select Photo from Gallery
    private fun selectPhoto() {
        binding.apply {
            imgProfile.setOnClickListener {
                resultLauncher.launch("image/*")
            }
            tvChangeProfile.setOnClickListener {
                resultLauncher.launch("image/*")
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
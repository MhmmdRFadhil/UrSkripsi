package com.capstone.urskripsi.ui.content.profile.account

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityChangeProfileBinding
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_EMAIL
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_STUDY_PROGRAM
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_UNIVERSITY_NAME
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.loadImageURI
import com.capstone.urskripsi.utils.Utility.loadImageUrl
import com.capstone.urskripsi.utils.Utility.setStateColor
import com.capstone.urskripsi.utils.Utility.show
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
    private fun uploadPhoto(uri: Uri?) {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        storageReference = FirebaseStorage.getInstance().getReference("User/$setEmail/Photo")

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val date = Date()
        val filename = formatter.format(date)
        val fileRef: StorageReference = storageReference.child(filename)

        if (uri != null) {
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    val map = HashMap<String, Any>()
                    map[FIREBASE_PHOTO] = it.toString()
                    databaseReference.updateChildren(map)
                    binding.progressBar.root.hide()
                    showToast(getString(R.string.save_success), this@ChangeProfileActivity)
                }
            }.addOnProgressListener {
                binding.progressBar.root.show()
            }.addOnFailureListener {
                binding.progressBar.root.hide()
                showToast(getString(R.string.save_failed), this@ChangeProfileActivity)
            }
        }
    }

    // Retrieve Data from Firebase
    private fun retrieveData() {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.keepSynced(true)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val photo = snapshot.child(FIREBASE_PHOTO).value
                    val name = snapshot.child(FIREBASE_NAME).value
                    val email = snapshot.child(FIREBASE_EMAIL).value
                    val universityName = snapshot.child(FIREBASE_UNIVERSITY_NAME).value
                    val studyProgram = snapshot.child(FIREBASE_STUDY_PROGRAM).value
                    binding.apply {
                        edtEmail.setText(email.toString())
                        edtName.setText(name.toString())
                        if (universityName != null && studyProgram != null) {
                            edtUniversityName.setText(universityName.toString())
                            edtStudyProgram.setText(studyProgram.toString())
                        }

                        if (photo != null) {
                            imgProfile.loadImageUrl(photo.toString())
                        } else {
                            imgProfile.setImageResource(R.drawable.avatar)
                        }

                        checkInput(
                            name.toString(),
                            email.toString(),
                            universityName.toString(),
                            studyProgram.toString(),
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
        studyProgram: String?,
    ) {
        binding.apply {
            val edt = listOf(edtName, edtEmail, edtUniversityName, edtStudyProgram)

            for (editText in edt) {

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val editTextName = edtName.text.toString().trim()
                        val editTextEmail = edtEmail.text.toString().trim()
                        val editTextUniversityName = edtUniversityName.text.toString().trim()
                        val editTextStudyProgram = edtStudyProgram.text.toString().trim()

                        if ((editTextName.isNotEmpty() && editTextName != name) ||
                            (editTextEmail.isNotEmpty() && editTextEmail != email) ||
                            (editTextUniversityName.isNotEmpty() && editTextUniversityName != universityName) ||
                            (editTextStudyProgram.isNotEmpty() && editTextStudyProgram != studyProgram)
                        ) {
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
        }
        saveDataUser(name, email, universityName, studyProgram)
        binding.toolbar.btnSave.setStateColor(this, R.color.button_disable, false)
    }
    // End

    // Save Data User to Firebase
    private fun saveDataUser(
        name: String?,
        email: String?,
        universityName: String?,
        studyProgram: String?
    ) {
        binding.apply {
            toolbar.btnSave.setOnClickListener {

                val txtName = edtName.text.toString().trim()
                val txtEmail = edtEmail.text.toString().trim()
                val txtUniversityName = edtUniversityName.text.toString().trim()
                val txtStudyProgram = edtStudyProgram.text.toString().trim()

                if (imgUri != null) {
                    if ((txtName != name) ||
                        (txtEmail != email) ||
                        (txtUniversityName != universityName) ||
                        (txtStudyProgram != studyProgram)
                    ) {
                        val map = HashMap<String, Any>()
                        map[FIREBASE_NAME] = txtName
                        map[FIREBASE_EMAIL] = txtEmail
                        map[FIREBASE_UNIVERSITY_NAME] = txtUniversityName
                        map[FIREBASE_STUDY_PROGRAM] = txtStudyProgram

                        databaseReference.updateChildren(map)
                        uploadPhoto(imgUri)
                        imgUri = null
                    } else {
                        uploadPhoto(imgUri)
                        imgUri = null
                    }
                } else {
                    val map = HashMap<String, Any>()
                    map[FIREBASE_NAME] = txtName
                    map[FIREBASE_EMAIL] = txtEmail
                    map[FIREBASE_UNIVERSITY_NAME] = txtUniversityName
                    map[FIREBASE_STUDY_PROGRAM] = txtStudyProgram

                    binding.progressBar.root.show()

                    databaseReference.updateChildren(map).addOnSuccessListener {
                        binding.progressBar.root.hide()
                        imgUri != null
                        showToast(getString(R.string.save_success), this@ChangeProfileActivity)
                    }.addOnFailureListener {
                        binding.progressBar.root.hide()
                        showToast(getString(R.string.save_failed), this@ChangeProfileActivity)
                    }
                }
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
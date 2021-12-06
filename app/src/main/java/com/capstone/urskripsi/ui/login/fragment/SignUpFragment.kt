package com.capstone.urskripsi.ui.login.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentSignUpBinding
import com.capstone.urskripsi.model.UserDataLogin
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            tvMasuk.setOnClickListener(this@SignUpFragment)
            btnDaftar.setOnClickListener(this@SignUpFragment)
        }
        mAuth = FirebaseAuth.getInstance()
    }

    private fun validateFirebaseUser() {
        binding?.apply {
            val name = edtNama.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
                edtEmail.error = resources.getString(R.string.invalid_formail_email)
            } else if (TextUtils.isEmpty(name)) {
                edtNama.error = resources.getString(R.string.name_empty)
            } else if (TextUtils.isEmpty(email)) {
                edtEmail.error = resources.getString(R.string.email_empty)
            } else if (TextUtils.isEmpty(password)) {
                edtPassword.error = resources.getString(R.string.password_empty)
            } else {
                progressBarDialog.root.show()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        // if signup success
                        progressBarDialog.root.hide()
                        val emailUser = mAuth.currentUser?.email
                        if (it.additionalUserInfo?.isNewUser as Boolean) {
                            saveDataUserLogin(emailUser.toString(), name)
                            changeFragmentToSignIn()
                            showToast(resources.getString(R.string.signup_success), requireContext())
                        } else {
                            showToast(resources.getString(R.string.signup_success), requireContext())
                        }
                    }
                    .addOnFailureListener { e ->
                        // if signup failed
                        progressBarDialog.root.hide()

                        showToast(
                            resources.getString(R.string.signup_failed, e.message),
                            requireContext()
                        )
                    }
            }
        }
    }

    private fun saveDataUserLogin(email: String, name: String) {
        val setEmail = email.replace('.', ',')

        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        val userDataLogin = UserDataLogin(email, name)
        databaseReference.setValue(userDataLogin)
    }

    private fun changeFragmentToSignIn() {
        val mLoginFragment = SignInFragment()
        val mFragmentManager = parentFragmentManager
        mFragmentManager.beginTransaction().apply {
            replace(
                R.id.frame_layout,
                mLoginFragment,
                SignInFragment::class.java.simpleName
            )
            commit()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                binding?.tvMasuk?.id -> changeFragmentToSignIn()

                binding?.btnDaftar?.id -> validateFirebaseUser()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
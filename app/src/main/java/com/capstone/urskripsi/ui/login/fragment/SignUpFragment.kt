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
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth

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
        val namaLengkap = binding?.edtNamaLengkap?.text.toString().trim()
        val email = binding?.edtEmail?.text.toString().trim()
        val password = binding?.edtPassword?.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
            binding?.edtEmail?.error = resources.getString(R.string.invalid_formail_email)
        } else if (TextUtils.isEmpty(namaLengkap)) {
            binding?.edtNamaLengkap?.error = resources.getString(R.string.name_empty)
        } else if (TextUtils.isEmpty(email)) {
            binding?.edtEmail?.error = resources.getString(R.string.email_empty)
        } else if (TextUtils.isEmpty(password)) {
            binding?.edtPassword?.error = resources.getString(R.string.password_empty)
        } else {
            showProgressBarDialog(true)
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // if signup success
                    showProgressBarDialog(false)

                    changeFragmentToSignIn()
                    showToast(resources.getString(R.string.signup_success), requireContext())
                }
                .addOnFailureListener { e ->
                    // if signup failed
                    showProgressBarDialog(false)

                    showToast(
                        resources.getString(R.string.signup_failed, e.message),
                        requireContext()
                    )
                }
        }
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

    private fun showProgressBarDialog(state: Boolean) {
        binding?.progressBarDialog?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE
        binding?.progressBarDialog?.tvTunggu?.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
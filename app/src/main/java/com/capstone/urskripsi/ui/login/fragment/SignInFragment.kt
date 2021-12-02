package com.capstone.urskripsi.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentSignInBinding
import com.capstone.urskripsi.ui.login.ForgotPasswordActivity
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            tvDaftar.setOnClickListener(this@SignInFragment)
            tvLupaPassword.setOnClickListener(this@SignInFragment)
            btnMasuk.setOnClickListener(this@SignInFragment)
        }

        mAuth = FirebaseAuth.getInstance()
        checkUserIfAlreadyLogin()
    }

    private fun checkUserIfAlreadyLogin() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateFirebaseUser() {
        val email = binding?.edtEmail?.text.toString().trim()
        val password = binding?.edtPassword?.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
            binding?.edtEmail?.error = resources.getString(R.string.invalid_formail_email)
        } else if (TextUtils.isEmpty(email)) {
            binding?.edtEmail?.error = resources.getString(R.string.email_empty)
        } else if (TextUtils.isEmpty(password)) {
            binding?.edtPassword?.error = resources.getString(R.string.password_empty)
        } else {
            showProgressBarDialog(true)
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // if success
                    showProgressBarDialog(false)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    // if failed
                    showProgressBarDialog(false)
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.login_failed) + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_daftar -> {
                    val mSignUpFragment = SignUpFragment()
                    val mFragmentManager = parentFragmentManager
                    mFragmentManager.popBackStackImmediate(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    mFragmentManager.beginTransaction().apply {
                        addToBackStack(null)
                        replace(
                            R.id.frame_layout,
                            mSignUpFragment,
                            SignUpFragment::class.java.simpleName
                        )
                        commit()
                    }
                }

                R.id.tv_lupa_password -> {
                    val intent = Intent(context, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_masuk -> validateFirebaseUser()
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
package com.capstone.urskripsi.ui.login.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentSignInBinding
import com.capstone.urskripsi.ui.login.ForgotPasswordActivity
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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
            btnGoogle.setOnClickListener(this@SignInFragment)
        }

        mAuth = FirebaseAuth.getInstance()
        checkUserIfAlreadyLogin()
        validateUserGoogleClient()
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

                    showToast(resources.getString(R.string.login_success), requireContext())
                }
                .addOnFailureListener { e ->
                    // if failed
                    showProgressBarDialog(false)
                    showToast(resources.getString(R.string.login_failed, e.message), requireContext())
                }
        }
    }

    private fun validateUserGoogleClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

    }

    private fun firebaseAuthWithGoogleAccount(accountClient: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(accountClient?.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                // if success
                // check if user is new or existing
                if (it.additionalUserInfo!!.isNewUser) {
                    showToast(resources.getString(R.string.google_new), requireContext())
                } else {
                    showToast(resources.getString(R.string.google_exist), requireContext())
                }

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .addOnFailureListener { e ->
                // if failed
                showToast(e.message.toString(), requireContext())
            }
    }

    private fun signIn() {
        val resultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = accountTask.getResult(ApiException::class.java)
                        firebaseAuthWithGoogleAccount(account)
                    } catch (e: Exception) {
                        showToast(e.message.toString(), requireContext())
                    }
                }
            }

        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
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

                R.id.btn_google -> signIn()
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
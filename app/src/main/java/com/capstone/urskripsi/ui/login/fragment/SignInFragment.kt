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
import com.capstone.urskripsi.helper.PreferencesHelper
import com.capstone.urskripsi.ui.login.ForgotPasswordActivity
import com.capstone.urskripsi.utils.Constant
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_EMAIL
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.Constant.Companion.PREF_USER_LOGIN
import com.capstone.urskripsi.utils.Utility.getStringFromName
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: PreferencesHelper
    private lateinit var databaseReference: DatabaseReference

    private val resultLauncher: ActivityResultLauncher<Intent> =
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

        sharedPreferences = PreferencesHelper(requireContext(), PREF_USER_LOGIN)

        mAuth = FirebaseAuth.getInstance()
        checkUserIfAlreadyLogin()
        validateUserGoogleClient()
        getDataUser()
    }

    private fun checkUserIfAlreadyLogin() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            signInSuccess()
        }
    }

    // login with Email
    private fun validateFirebaseUser() {
        binding?.apply {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
                edtEmail.error = resources.getString(R.string.invalid_formail_email)
            } else if (TextUtils.isEmpty(email)) {
                edtEmail.error = resources.getString(R.string.email_empty)
            } else if (TextUtils.isEmpty(password)) {
                edtPassword.error = resources.getString(R.string.password_empty)
            } else {
                progressBarDialog.root.show()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        // if success
                        progressBarDialog.root.hide()
                        showToast(resources.getString(R.string.login_success), requireContext())
                        signInSuccess()
                    }
                    .addOnFailureListener { e ->
                        // if failed
                        progressBarDialog.root.hide()
                        showToast(
                            resources.getString(R.string.login_failed, e.message),
                            requireContext()
                        )
                    }

                if (email.isNotEmpty() && password.isNotEmpty() && cbIngatkanSaya.isChecked) {
                    setDataUser(email, password)
                } else {
                    sharedPreferences.clearData()
                }
            }
        }
    }
    // End

    // Login with Google
    private fun validateUserGoogleClient() {
        val defaultWebClientId =
            requireContext().getStringFromName(getString(R.string.web_client_id))
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

    }

    private fun firebaseAuthWithGoogleAccount(accountClient: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(accountClient?.idToken, null)
        binding?.progressBarDialog?.root?.show()
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                // if success
                // check if user is new or existing
                binding?.progressBarDialog?.root?.hide()
                val email = mAuth.currentUser?.email
                if (it.additionalUserInfo?.isNewUser as Boolean) {
                    saveDataUserLogin(email.toString())
                    showToast(resources.getString(R.string.google_new), requireContext())
                } else {
                    showToast(resources.getString(R.string.google_exist), requireContext())
                }

                signInSuccess()
            }
            .addOnFailureListener { e ->
                // if failed
                binding?.progressBarDialog?.root?.hide()
                showToast(e.message.toString(), requireContext())
            }
    }
    // End

    private fun saveDataUserLogin(email: String) {
        val firebaseUser = mAuth.currentUser
        val setEmail = email.replace('.', ',')
        val getPhoto = firebaseUser?.photoUrl?.toString()
        val getName = firebaseUser?.displayName.toString()
        val resizePhoto = getPhoto?.replace("s96-c", "s400-c").toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        val map = HashMap<String, Any>()
        map[FIREBASE_EMAIL] = email
        map[FIREBASE_NAME] = getName
        map[FIREBASE_PHOTO] = resizePhoto
        databaseReference.setValue(map)
    }

    // shared preference to save data login
    private fun setDataUser(email: String, password: String) {
        sharedPreferences.setDataString(Constant.PREF_EMAIL, email)
        sharedPreferences.setDataString(Constant.PREF_PASSWORD, password)
        sharedPreferences.setDataBoolean(Constant.PREF_IS_CHECKED, true)
    }

    private fun getDataUser() {
        binding?.apply {
            val email = sharedPreferences.getDataString(Constant.PREF_EMAIL)
            val password = sharedPreferences.getDataString(Constant.PREF_PASSWORD)
            val isChecked = sharedPreferences.getDataBoolean(Constant.PREF_IS_CHECKED)

            if (isChecked) {
                edtEmail.setText(email)
                edtPassword.setText(password)
                cbIngatkanSaya.isChecked = true
            }
        }
    }
    // End

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

    private fun signInSuccess() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
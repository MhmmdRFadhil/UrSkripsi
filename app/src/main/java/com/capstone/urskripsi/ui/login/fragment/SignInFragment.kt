package com.capstone.urskripsi.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentSignInBinding
import com.capstone.urskripsi.ui.login.ForgotPasswordActivity

class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when(view.id) {
                R.id.tv_daftar -> {
                    val mSignUpFragment = SignUpFragment()
                    val mFragmentManager = parentFragmentManager
                    mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

                R.id.btn_masuk -> {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
package com.example.cocygo.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentSignInBinding
import com.example.cocygo.homeFragment.HomeFragment
import com.example.cocygo.signUp.SignUpFragment

class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null

    // ViewModel
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the ViewModel
        signInViewModel = ViewModelProvider(requireActivity())[SignInViewModel::class.java]

        binding?.signInBtn?.setOnClickListener {
            val email = binding!!.emailEt.text.toString()
            val pass = binding!!.passET.text.toString()
            signInViewModel.signIn(requireActivity(), email, pass)
        }
        binding?.tvSignUp?.setOnClickListener {
            val signUpFragment = SignUpFragment()
            //  addToBackStack(null) allows the user to press the back button to return to FirstFragment.
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, signUpFragment)
                .addToBackStack(null)
                .commit()
        }

        // Observe the signInFlag LiveData
        signInViewModel.signInFlag.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Handle successful sign-in (e.g., navigate to another fragment or activity)
                val homeFragment = HomeFragment()
                //  addToBackStack(null) allows the user to press the back button to return to FirstFragment.
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                // Handle failed sign-in
                // The error message will be shown via the signInErrorMSG observer
            }
        }

        // Observe the signInErrorMSG LiveData
        signInViewModel.signInErrorMSG.observe(viewLifecycleOwner) { signInErrorMSG ->
            binding?.errMsg?.text = signInErrorMSG
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
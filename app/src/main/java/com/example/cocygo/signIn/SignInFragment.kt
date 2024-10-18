package com.example.cocygo.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentSignInBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null

    //    ViewModel
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sign_in, container, false)
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //notice here the ViewModelProvider should be requireActivity() then it can been observed by host Activity
        signInViewModel = ViewModelProvider(requireActivity())[SignInViewModel::class.java]

        binding?.button?.setOnClickListener {
            val email = binding!!.emailEt.text.toString()
            val pass = binding!!.passET.text.toString()

            signInViewModel.signIn(requireActivity(), email, pass)

        }

        signInViewModel.signInErrorMSG.observe(requireActivity()){ signInErrorMSG ->
            binding?.errMsg?.text = signInErrorMSG
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
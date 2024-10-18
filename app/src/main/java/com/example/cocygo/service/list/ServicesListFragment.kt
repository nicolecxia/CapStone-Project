package com.example.cocygo.service.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentServicesListBinding
import com.example.cocygo.service.detail.ServiceDetailFragment
import com.example.cocygo.service.detail.ServiceDetailViewModel
import com.example.cocygo.signIn.SignInViewModel
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 * Use the [ServicesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServicesListFragment : Fragment() {
    private var binding: FragmentServicesListBinding? = null
    lateinit var firebaseAuth: FirebaseAuth

    //ViewModel
    private lateinit var servicesListViewModel: ServicesListViewModel
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var serviceDetailViewModel: ServiceDetailViewModel

    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_services_list, container, false)
        binding = FragmentServicesListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        //notice here the ViewModelProvider should be requireActivity() then it can been observed by host Activity
        servicesListViewModel =
            ViewModelProvider(requireActivity())[ServicesListViewModel::class.java]
        signInViewModel = ViewModelProvider(requireActivity())[SignInViewModel::class.java]
        serviceDetailViewModel =
            ViewModelProvider(requireActivity())[ServiceDetailViewModel::class.java]

        binding?.tvUserInfo?.text = firebaseAuth.currentUser?.email.toString()
        binding?.btnSignOut?.setOnClickListener {
            signInViewModel.signOut()
        }

        binding?.btnAdd?.setOnClickListener {
//            servicesListViewModel.addToCart("123","Test add to cart")

//            servicesListViewModel.getCartByUser("123")

            servicesListViewModel.getServiceList()

//            servicesListViewModel.duplicateDocument("ServiceList/yD2O2smrV8TYD3eTQRmd","ServiceList/XUwXDjBNl96dnYmVLp7g")
        }

        listinLiveData()

        binding?.textView3?.setOnClickListener {
            //Todo: update the currect service


            //transaction to new fragment
            val serviceDetailFragment = ServiceDetailFragment()
            //  addToBackStack(null) allows the user to press the back button to return to FirstFragment.
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, serviceDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun listinLiveData() {
        servicesListViewModel.cartLists.observe(requireActivity()) { cartLists ->
            var value: String = ""
            for (cart in cartLists) {
                value += "${cart.userID}/${cart.serviceID} \n"
            }
            binding?.textView3?.text = value
        }

        servicesListViewModel.serviceLists.observe(requireActivity()) { serviceLists ->
            var value: String = ""
            for (service in serviceLists) {
                value += "${service.serviceName}/${service.stylist} \n"
            }
            binding?.textView3?.text = value
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServicesListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
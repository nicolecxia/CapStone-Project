package com.example.cocygo.service.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentServiceDetailBinding
import com.example.cocygo.service.list.ServiceAdapter
import com.example.cocygo.service.list.ServicesListViewModel
import com.example.cocygo.service.model.Base64ToBitmap


class ServiceDetailFragment : Fragment() {
    private var binding: FragmentServiceDetailBinding? = null

    //ViewModel
    private lateinit var serviceDetailViewModel: ServiceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_service_detail, container, false)
        binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceDetailViewModel =
            ViewModelProvider(requireActivity())[ServiceDetailViewModel::class.java]

        serviceDetailViewModel.serviceInfo.observe((requireActivity())){service ->

            binding?.title?.text = service.serviceName
            binding?.subhead?.text = "Duration: ${service.duration}mins\nStylist: ${service.stylist}"
            binding?.body?.text = service.description

            //show image
            val imageName = service.image
            val bitmap = Base64ToBitmap.decodeBase64ToBitmap(imageName)
            binding?.headerImage?.setImageBitmap(bitmap)
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
            ServiceDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
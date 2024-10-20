package com.example.cocygo.detailFragment.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.capstonefragment.viewModel.ServicesListViewModel
import com.example.cocygo.databinding.FragmentServiceDetailBinding


class ServiceDetailFragment(image: String?, name: String?, tittle: String?) : Fragment() {
    private var binding: FragmentServiceDetailBinding? = null
    var name = name
    var image = image
    var tittle = tittle

    //ViewModel
    private lateinit var servicesListViewModel: ServicesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesListViewModel =
            ViewModelProvider(requireActivity())[ServicesListViewModel::class.java]
        val bitmap = decodeBase64ToBitmap(image!!.trim())

        binding?.title?.text = tittle
        binding?.headerImage?.setImageBitmap(bitmap)
        binding?.body?.text = name
        servicesListViewModel.serviceLists.observe(requireActivity()) { serviceLists ->
//            binding?.title?.text = serviceLists[0].serviceName
//            binding?.subhead?.text = serviceLists[0].stylist
//            binding?.body?.text = serviceLists[0].description


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ServiceDetailFragment(item.image, item.name, item.tittle).apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }
    private fun decodeBase64ToBitmap(base64: String): Bitmap? {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
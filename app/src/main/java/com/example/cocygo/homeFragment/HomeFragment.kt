package com.example.cocygo.homeFragment

import LanguageFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.R
import com.example.cocygo.detailFragment.view.ServiceDetailFragment
import com.example.cocygo.homeFragment.adapter.ItemAdapter
import com.example.cocygo.homeFragment.adapter.ItemViewModel
import com.example.cocygo.homeFragment.adapter.ServicesListViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: ItemViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var servicesListViewModel: ServicesListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(ItemViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter = ItemAdapter( items) { item ->
                Log.d("Change liked visibility:", item.likeStatus.toString())
                // Handle item click if needed
                // Toast.makeText(requireContext(), "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
                val serviceDetailFragment = ServiceDetailFragment(item.id, item.image, item.name, item.tittle, item.likeStatus)
                // addToBackStack(null) allows the user to press the back button to return to FirstFragment.
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, serviceDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
            recyclerView.adapter = adapter
        }
        if (childFragmentManager.findFragmentById(R.id.language_fragment_container) == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.language_fragment_container, LanguageFragment())
                .commit()
        }
    }
}
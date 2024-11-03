package com.example.cocygo.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.cocygo.MainActivity
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentSliderBinding
import com.example.cocygo.homeFragment.HomeFragment
import com.example.cocygo.signIn.SignInFragment
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class SlidePageMenuFragment : Fragment() {
    private var name = ""
    private var userLoggedIn: Boolean = true
    private lateinit var binding: FragmentSliderBinding // Declare View Binding
    private lateinit var vp: ViewPager2
    private lateinit var switchButton: ViewPager2.OnPageChangeCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        binding = FragmentSliderBinding.inflate(inflater, container, false)

        vp = binding.slideVP
        val dotsSlide: DotsIndicator = binding.dotsIndicator
        val pagerAdapter = ViewPageAdapter(requireActivity()) { name = it.toString() }
        vp.adapter = pagerAdapter
        dotsSlide.setViewPager2(vp)

        // Show button finish in Slide 4 and button next in another slide
        val btnFinish = binding.buttonFinish
        val btnNext = binding.buttonNext

        switchButton = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    btnFinish.visibility = View.VISIBLE
                    btnNext.visibility = View.GONE
                } else {
                    btnNext.visibility = View.VISIBLE
                    btnFinish.visibility = View.GONE
                }
            }
        }
        vp.registerOnPageChangeCallback(switchButton)

        // Button next used to swipe the slide until slide 4
        btnNext.setOnClickListener {
            if (vp.currentItem < 3) {
                vp.currentItem += 1
            }
        }

        // Button finish used to finish all
        btnFinish.setOnClickListener {
            if (name.isNotEmpty()) {
                Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra("dataName", name)
                    startActivity(this)
                }
            } else {
                if (userLoggedIn) {
                    //transaction to new fragment
//                    val homeFragment = HomeFragment()
                    val homeFragment = SignInFragment()
                    //  addToBackStack(null) allows the user to press the back button to return to FirstFragment.
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }


    return binding.root // Return the root view from the binding
}

// Used to destroy onPageChangeCallback to avoid memory leaks
override fun onDestroyView() {
    super.onDestroyView()
    vp.unregisterOnPageChangeCallback(switchButton)
}
}
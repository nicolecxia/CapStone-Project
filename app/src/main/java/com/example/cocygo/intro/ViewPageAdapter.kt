package com.example.slidepage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cocygo.intro.IntroSliderFragment

class ViewPageAdapter(Fa:FragmentActivity,listener:(CharSequence)->Unit):FragmentStateAdapter(Fa) {
    private val dataFragments= mutableListOf(
        IntroSliderFragment.newInstance("0",listener),
        IntroSliderFragment.newInstance("1",listener),
        IntroSliderFragment.newInstance("2",listener),
        IntroSliderFragment.newInstance("3",listener)
    )
    override fun getItemCount(): Int =4

    override fun createFragment(position: Int): Fragment =dataFragments[position]
}
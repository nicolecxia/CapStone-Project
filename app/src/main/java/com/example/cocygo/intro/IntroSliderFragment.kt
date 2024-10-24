package com.example.cocygo.intro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.cocygo.R

private const val ARG_PARAM1 = "param1"

class IntroSliderFragment : Fragment() {

    private var param1: String? = null
    private lateinit var listener: (CharSequence) -> Unit

    //used to create argument for Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    //Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_intro_slider, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //used to process slide by slide from fragment. it means we can do anything from this code
        val textFragment by lazy {
            mutableListOf<TextView>(
                view.findViewById(R.id.text_up),
            )
        }
        val imageFragment = view.findViewById<ImageView>(R.id.imageFragment)
        when (param1) {
            "3" -> {
              //  textFragment[0].visibility = View.GONE
                textFragment[0].text = resources.getString(R.string.text_intro4)
                imageFragment.setBackgroundResource(R.drawable.ic_rest)

            }
            "2" -> {
                textFragment[0].text = resources.getString(R.string.text_intro3)
                imageFragment.setBackgroundResource(R.drawable.ic_angry)
            }
            "1" -> {
                textFragment[0].text = resources.getString(R.string.text_intro2)
                imageFragment.setBackgroundResource(R.drawable.ic_tired)
            }
            else -> {
                textFragment[0].text = resources.getString(R.string.text_intro1)
                imageFragment.setBackgroundResource(R.drawable.ic_sad)
            }
        }
    }

    //Companion object used to adapter call fragment and don't need to create object , just call the object
    companion object {
        @JvmStatic
        fun newInstance(param1: String, listener: (CharSequence) -> Unit) =
            IntroSliderFragment().apply {
                this.listener = listener
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
package com.example.karbushev.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.karbushev.R
import com.example.karbushev.databinding.FragmentGifBinding
import com.example.karbushev.ui.MainViewModel
import com.example.karbushev.utils.LATEST
import com.example.karbushev.utils.LOG
import com.example.karbushev.utils.VIEW_MODEL


class LatestFragment : Fragment() {

    private var _binding: FragmentGifBinding? = null
    private val mBinding get() = _binding!!

    private var counter = 1
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGifBinding.inflate(layoutInflater, container, false)

        init()

        Log.d(LOG, counter.toString())

        return mBinding.root
    }

    private fun init() {
        VIEW_MODEL = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding.description.text = "text"

        setGif()

        mBinding.buttonNext.setOnClickListener {
            if (counter == 4) {
                page++
                counter = 0
            }
            setGif()
            Log.d(LOG, counter.toString())
        }
    }

    private fun setGif() {
        VIEW_MODEL.getGifs(LATEST, page)
        //mBinding.image.setImageResource()
        mBinding.image.setBackgroundColor(Color.BLACK)
        mBinding.description.text = "text"
        mBinding.description.text = VIEW_MODEL.data.value?.result?.get(counter)?.description
        counter++
        Log.d(LOG, counter.toString())
    }
}
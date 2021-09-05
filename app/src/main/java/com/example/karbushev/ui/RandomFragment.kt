package com.example.karbushev.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.karbushev.R
import com.example.karbushev.databinding.FragmentRandomBinding

class RandomFragment : Fragment() {

    private var _binding: FragmentRandomBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRandomBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }
}
package com.example.karbushev.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karbushev.databinding.FragmentLatestBinding


class LatestFragment : Fragment() {

    private var _binding: FragmentLatestBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLatestBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }
}
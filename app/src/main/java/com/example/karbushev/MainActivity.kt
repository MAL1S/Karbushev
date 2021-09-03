package com.example.karbushev

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.karbushev.databinding.ActivityMainBinding
import com.example.karbushev.databinding.FragmentGifBinding
import com.example.karbushev.ui.MainViewModel
import com.example.karbushev.utils.LOG
import com.example.karbushev.utils.VIEW_MODEL

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        init()

        VIEW_MODEL = ViewModelProvider(this).get(MainViewModel::class.java)

        VIEW_MODEL.data.observe(this) {
            Log.d(LOG, "really here")
            Log.d(LOG, it.result.toString())
        }
    }

    private fun init() {
        mBinding.bottomNav.setupWithNavController(findNavController(R.id.fragment_container))


    }
}
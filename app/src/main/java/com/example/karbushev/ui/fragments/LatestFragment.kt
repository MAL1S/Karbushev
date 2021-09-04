package com.example.karbushev.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.karbushev.data.Gif
import com.example.karbushev.databinding.FragmentGifBinding
import com.example.karbushev.ui.MainViewModel
import com.example.karbushev.utils.APP_ACTIVITY
import com.example.karbushev.utils.LATEST
import com.example.karbushev.utils.LOG
import com.example.karbushev.utils.showToast


class LatestFragment : Fragment() {

    private var _binding: FragmentGifBinding? = null
    private val mBinding get() = _binding!!

    private var counter = 0
    private var page = 0

    private var gifList: List<Gif>? = null

    private lateinit var mViewModel: MainViewModel

    private var wasLastNext = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGifBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG, "fragment")

        init()
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//        updateList()
//        setGif()
        mViewModel.getGifs(LATEST, page)
//        mBinding.description.text = gifList?.get(counter)?.description
//        counter++

        mViewModel.data.observe(APP_ACTIVITY) {
            //mBinding.description.text = it.result[counter].description
            gifList = it.result

            setNextGif()

            Log.d(LOG, "```````" + gifList.toString())
        }

        mBinding.buttonNext.setOnClickListener {
            mBinding.image.visibility = View.INVISIBLE
            if (counter == 5) {
                page++
                counter = 0
                updateList()
                //mBinding.buttonBack.isClickable = false
            }
            else {
                setNextGif()
                //mBinding.buttonBack.isClickable = true
            }
            mBinding.image.visibility = View.VISIBLE
        }

        mBinding.buttonBack.setOnClickListener {
            if (counter == 1 && wasLastNext || counter == 0 && !wasLastNext) showToast("В стеке нет гифок")
            else setPrevGif()
        }

        //mBinding.buttonBack.isClickable = false

//        mBinding.description.text = "text"
//
//        setGif()
//
//        mBinding.buttonNext.setOnClickListener {
//            if (counter == 4) {
//                page++
//                counter = 0
//            }
//            setGif()
//            Log.d(LOG, counter.toString())
//        }
    }

    private fun setNextGif() {
        if (!wasLastNext) counter++
        Glide
            .with(APP_ACTIVITY)
            .load(gifList?.get(counter)?.gifURL)
            .into(mBinding.image)
        mBinding.description.text = gifList?.get(counter)?.description
        counter++
        wasLastNext = true
    }

    private fun setPrevGif() {
        if (wasLastNext) {
            counter -= 2
            wasLastNext = false
        }
        else counter--
        Glide
            .with(APP_ACTIVITY)
            .load(gifList?.get(counter)?.gifURL)
            .into(mBinding.image)
        mBinding.description.text = gifList?.get(counter)?.description
    }

    private fun updateList() {
        mViewModel.getGifs(LATEST, page)
    }

//    private fun setGif() {
//        VIEW_MODEL.getGifs(LATEST, page)
//        Log.d(LOG, VIEW_MODEL.data.value?.result!!.toString())
//        //mBinding.image.setImageResource()
//        mBinding.image.setBackgroundColor(Color.BLACK)
//        mBinding.description.text = "text"
//        mBinding.description.text = VIEW_MODEL.data.value?.result?.get(counter)?.description
//        counter++
//        Log.d(LOG, counter.toString())
//    }
}
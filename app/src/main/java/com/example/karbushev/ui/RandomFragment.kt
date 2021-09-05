package com.example.karbushev.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.karbushev.R
import com.example.karbushev.data.Gif
import com.example.karbushev.databinding.FragmentGifBinding
import com.example.karbushev.utils.*
import java.util.*

class RandomFragment : Fragment() {

    private var _binding: FragmentGifBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: MainViewModel

    private var currentGif: Gif? = null

    private var gifList = mutableListOf<Gif>()

    private var currentGifIndex = -1

    private var wasLastNext = true

    private var state: String = ""

    private var rnd: Int = 0

    private var wasErrorLast = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGifBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        CURRENT_TAB = RANDOM

        mBinding.buttonRefresh.setOnClickListener {
            if (!checkInternetConnection()) {
                showToast(getString(R.string.no_internet))
            } else {
                mBinding.loading.visibility = View.VISIBLE
                if (rnd != 0) reloadGif()
                else init()
            }
        }

        if (!checkInternetConnection()) {
            Log.d(LOG, "no internet first step")
            showToast(getString(R.string.no_internet))
            mBinding.buttonBack.isClickable = false
            mBinding.buttonNext.isClickable = false
            showDisconnectedView()
            return
        } else showConnectedView()

        mViewModel.gif.observe(APP_ACTIVITY) {
            Log.d(LOG, "observed gif")
            if (it == null) {
                updateGif()

            } else {
                currentGifIndex++
                currentGif = it
                gifList.add(it)

                Log.d(LOG, "after = $gifList")


                wasLastNext = true

                setNextGif()

                //mBinding.buttonNext.isClickable = true
            }
        }

        mViewModel.getRandomGif(randomGifNumber())

        mViewModel.state.observe(APP_ACTIVITY) {
            state = it
            Log.d(LOG, "observed error = $it")
            if (it == "error") {
                wasErrorLast = true
                mBinding.buttonNext.isClickable = false
                if (currentGifIndex != 0) mBinding.buttonBack.isClickable = true
                showDisconnectedView()
            }
        }

        mBinding.buttonNext.setOnClickListener {
            mBinding.image.visibility = View.INVISIBLE
            mBinding.loading.visibility = View.VISIBLE
            mBinding.buttonNext.isClickable = false
            mBinding.buttonBack.isClickable = false
            if (currentGifIndex + 1 == gifList.size) updateGif()
            else {
                currentGifIndex++
                setNextGif()
            }
            //mBinding.image.visibility = View.VISIBLE
        }
        mBinding.buttonNext.isClickable = false

        mBinding.buttonBack.setOnClickListener {
            setPrevGif()
            if (currentGifIndex == 0) mBinding.buttonBack.isClickable = false
        }
        mBinding.buttonBack.isClickable = false
    }

    private fun setNextGif() {
        var gif = if (currentGifIndex + 1 < gifList.size) {
            gifList[currentGifIndex]
        } else currentGif!!
        Glide
            .with(APP_ACTIVITY)
            .load(gif.gifURL)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(true)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    wasErrorLast = true
                    showDisconnectedView()
                    if (currentGifIndex != 0) mBinding.buttonBack.isClickable = true
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    wasErrorLast = false
                    Log.d(LOG, "loaded")
                    showConnectedView()
                    mBinding.buttonNext.isClickable = true
                    if (currentGifIndex != 0) mBinding.buttonBack.isClickable = true
                    return false
                }

            })
            .into(mBinding.image)
        mBinding.description.text = gif.description
    }

    private fun setPrevGif() {
        //if (state == ERROR) currentGifIndex--
        if (!wasErrorLast) currentGifIndex--
        wasErrorLast = false
        Glide
            .with(APP_ACTIVITY)
            .load(gifList[currentGifIndex].gifURL)
            .onlyRetrieveFromCache(true)
            .into(mBinding.image)
        mBinding.description.text = gifList[currentGifIndex].description
        showConnectedView()
        mBinding.buttonNext.isClickable = true
    }

    private fun updateGif() {
        Log.d(LOG, "updated")
        rnd = randomGifNumber()
        Log.d(LOG, "$rnd")
        mViewModel.getRandomGif(rnd)
    }

    private fun reloadGif() {
        Log.d(LOG, "$rnd")
        mViewModel.getRandomGif(rnd)
    }

    private fun randomGifNumber() = (1..17000).random()

    private fun showDisconnectedView() {
        mBinding.loading.visibility = View.INVISIBLE
        mBinding.description.visibility = View.INVISIBLE
        mBinding.image.visibility = View.INVISIBLE

        mBinding.errorMessage.visibility = View.VISIBLE
        mBinding.buttonRefresh.visibility = View.VISIBLE
    }

    private fun showConnectedView() {
        mBinding.description.visibility = View.VISIBLE
        mBinding.image.visibility = View.VISIBLE
        //mBinding.loading.visibility = View.VISIBLE

        mBinding.errorMessage.visibility = View.INVISIBLE
        mBinding.buttonRefresh.visibility = View.INVISIBLE
    }
}
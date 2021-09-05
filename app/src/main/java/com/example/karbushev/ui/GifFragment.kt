package com.example.karbushev.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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


class GifFragment : Fragment() {
    private var _binding: FragmentGifBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var cat: String

    private var counter = 0
    private var page = 0

    private var gifList: List<Gif>? = null

    private lateinit var mViewModel: MainViewModel

    private var wasLastNext = true

    private var fromButton = false
    private var fromUpdating = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGifBinding.inflate(layoutInflater, container, false)

        Log.d(LOG, "created")

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        Log.d(LOG, "started")
        init()
    }

    private fun init() {
        mBinding.buttonRefresh.setOnClickListener {
            if (!fromButton) init()
            else {
                if (fromUpdating) updateList()
                else {
                    counter--
                    next()
                }
            }
        }

        if (!checkInternetConnection()) {
            Log.d(LOG, "no internet first step")
            showToast("Не удалось подключиться к интернету")
            fromButton = false
            mBinding.buttonBack.isClickable = false
            mBinding.buttonNext.isClickable = false
            showDisconnectedView()
            return
        } else showConnectedView()

        mBinding.buttonBack.isClickable = false

        CURRENT_TAB = TAB_TITLES[getSelectedItem(APP_ACTIVITY.bottomNav)-1]

        if (CURRENT_TAB == HOT) {
            hotTabError()
            return
        }

        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mViewModel.getGifs(CURRENT_TAB, page)
        //Log.d(LOG, "list = " + gifList.toString())

        mViewModel.data.observe(APP_ACTIVITY) {
            gifList = it.result

            setNextGif()
            showConnectedView()
            mBinding.buttonNext.isClickable = true

            Log.d(LOG, "```````" + it.result.toString())
        }

        mViewModel.state.observe(APP_ACTIVITY) {
            Log.d(LOG, "observed error = $it")
            if (it == ERROR) {
                mBinding.buttonNext.isClickable = false
                showDisconnectedView()
            }
        }

        mBinding.buttonNext.setOnClickListener {
            fromButton = true
            next()
        }

        mBinding.buttonBack.setOnClickListener {
//            if (counter == 1 && wasLastNext || counter == 0 && !wasLastNext) showToast("В стеке нет гифок")
//            else setPrevGif()
            setPrevGif()
            if (counter == 0) mBinding.buttonBack.isClickable = false
        }
        mBinding.buttonBack.isClickable = false
    }

    private fun next() {
        Log.d(LOG, "here")
//        if (!checkInternetConnection()) {
//            fromButton = true
//            showDisconnectedView()
//            showToast("Не удалось подключиться к интернету")
//        } else {

        mBinding.image.visibility = View.INVISIBLE
        mBinding.loading.visibility = View.VISIBLE
        mBinding.buttonNext.isClickable = false
        if (counter == 5) {
            page++
            counter = 0
            Log.d(LOG, "error in next")
            updateList()
            mBinding.buttonBack.isClickable = false
        } else {
            setNextGif()
            mBinding.buttonBack.isClickable = true
        }
        Log.d(LOG, "NO")
        mBinding.image.visibility = View.VISIBLE
        mBinding.buttonNext.isClickable = true
        //mBinding.loading.visibility = View.INVISIBLE
        //}
    }

    private fun setNextGif() {
        Log.d(LOG, "$counter")
        if (!wasLastNext) counter++
        Glide
            .with(APP_ACTIVITY)
            .load(gifList?.get(counter)?.gifURL)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(true)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showDisconnectedView()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    fromUpdating = false
                    showConnectedView()
                    mBinding.buttonNext.isClickable = true
                    return false
                }

            })
            .into(mBinding.image)
        mBinding.description.text = gifList?.get(counter)?.description
        counter++
        Log.d(LOG, "counter changed = $counter")
        wasLastNext = true
    }

    private fun setPrevGif() {
        if (wasLastNext) {
            counter -= 2
            wasLastNext = false
        } else counter--
        Glide
            .with(APP_ACTIVITY)
            .load(gifList?.get(counter)?.gifURL)
            .onlyRetrieveFromCache(true)
            .into(mBinding.image)
        mBinding.description.text = gifList?.get(counter)?.description
    }

    private fun updateList() {
        Log.d(LOG, "updated")
        fromUpdating = true
        mViewModel.getGifs(CURRENT_TAB, page)
    }

    private fun hotTabError() {
        mBinding.description.text =
            getString(R.string.error_hot)
        mBinding.image.visibility = View.INVISIBLE
        mBinding.buttonNext.isClickable = false
    }

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



    override fun onStop() {
        super.onStop()
        if (CURRENT_TAB != HOT) mViewModel.data.removeObservers(APP_ACTIVITY)
    }
}
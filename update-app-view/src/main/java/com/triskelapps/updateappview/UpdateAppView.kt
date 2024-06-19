package com.triskelapps.updateappview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.triskelapps.updateappview.databinding.ViewUpdateAppBinding

class UpdateAppView(context: Context, attrs: AttributeSet, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    private var updateAppManager = UpdateAppManager(context)
    private var binding =  ViewUpdateAppBinding.inflate(LayoutInflater.from(context), this, true)

    private val lifecycleObserver: LifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)

            updateAppManager.onResume()
        }
    }
    
    init {

        binding.btnUpdateApp.setOnClickListener { v: View? -> updateAppManager.onUpdateVersionClick() }
        binding.btnCloseUpdateAppView.setOnClickListener { v: View? -> visibility = GONE }

        visibility = GONE

        configure() 
    }


    private fun configure() {
        updateAppManager = UpdateAppManager(context)
        updateAppManager.setUpdateAvailableListener { visibility = VISIBLE }

        if (context is Activity) {
            checkUpdateAvailable()
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i(TAG, "onAttachedToWindow: ")
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).lifecycle.addObserver(lifecycleObserver)
        }
    }

    override fun onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ")
        super.onDetachedFromWindow()
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).lifecycle.removeObserver(lifecycleObserver)
        }
    }


    private fun checkUpdateAvailable() {
        updateAppManager.checkUpdateAvailable()
    }

    companion object {
        private val TAG: String = UpdateAppView::class.java.simpleName
    }
}

package com.triskelapps.updateappview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.triskelapps.updateappview.databinding.ViewUpdateAppBinding
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send

class UpdateAppView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var updateAppManager = UpdateAppManager(context)
    private var binding = ViewUpdateAppBinding.inflate(LayoutInflater.from(context), this, true)

    private val lifecycleObserver: LifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)

            updateAppManager.onResume()
        }
    }

    init {

        binding.btnUpdateApp.setOnClickListener { updateAppManager.onUpdateVersionClick() }
        binding.btnCloseUpdateAppView.setOnClickListener { visibility = GONE }

        UpdateAppManager.updateBarStyle?.let {
            binding.root.setBackgroundColor(ContextCompat.getColor(context, it.backgroundColor))
            binding.tvNewVersionAvailable.setTextColor(ContextCompat.getColor(context, it.foregroundElementsColor))
            binding.btnUpdateApp.setTextColor(ContextCompat.getColor(context, it.foregroundElementsColor))
            binding.btnCloseUpdateAppView.setColorFilter(ContextCompat.getColor(context, it.foregroundElementsColor))

            it.textStyle?.let {style ->
                binding.tvNewVersionAvailable.setTextAppearance(context, style)
                binding.btnUpdateApp.setTextAppearance(context, style)
            }
        }

        visibility = GONE

        configure()
    }


    private fun configure() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (context as AppCompatActivity).apply {
                val permissionRequest = permissionsBuilder(Manifest.permission.POST_NOTIFICATIONS).build()
                permissionRequest.send()
            }
        }
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

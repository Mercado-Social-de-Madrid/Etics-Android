package net.mercadosocial.moneda.util.update_app;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import net.mercadosocial.moneda.BuildConfig;
import net.mercadosocial.moneda.databinding.ViewUpdateAppBinding;

public class UpdateAppView extends FrameLayout {

    private static final String TAG = UpdateAppView.class.getSimpleName();

    private UpdateAppManager updateAppManager;
    private ViewUpdateAppBinding binding;

    private LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            DefaultLifecycleObserver.super.onResume(owner);

            if (BuildConfig.DEBUG) {
                return;
            }

            updateAppManager.onResume();

        }

    };

    public UpdateAppView(Context context) {
        super(context);
        init();
    }


    public UpdateAppView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpdateAppView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        binding = ViewUpdateAppBinding.inflate(LayoutInflater.from(getContext()), this, true);

        binding.btnUpdateApp.setOnClickListener(v -> updateAppManager.onUpdateVersionClick());
        binding.btnCloseUpdateAppView.setOnClickListener(v -> setVisibility(GONE));

        setVisibility(GONE);

        configure();
    }

    private void configure() {

        updateAppManager = new UpdateAppManager(getContext());
        updateAppManager.setUpdateAvailableListener(() -> setVisibility(VISIBLE));

        if (getContext() instanceof Activity) {
            checkUpdateAvailable();
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().addObserver(lifecycleObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().removeObserver(lifecycleObserver);
        }
    }


    private void checkUpdateAvailable() {

        if (BuildConfig.DEBUG) {
            return;
        }

        updateAppManager.checkUpdateAvailable();

    }

}

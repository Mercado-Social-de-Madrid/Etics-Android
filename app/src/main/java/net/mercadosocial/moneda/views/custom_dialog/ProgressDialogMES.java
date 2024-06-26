package net.mercadosocial.moneda.views.custom_dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.views.RotativeImageView;

/**
 * Created by julio on 3/03/18.
 */

public class ProgressDialogMES extends DialogFragment {


    public static final long MIN_DELAY = 100;
    private static final long MIN_SHOW_TIME = 600;

    private static final String TAG = "ProgressDialogMES";
    private static ProgressDialogMES progressInstance;
    private static ProgressDialogMES sInstance;

    private long timeStart = -1;
    boolean postedHide = false;
    boolean postedShow = false;
    boolean dismissed = false;

    private FragmentManager fragmentManager;
    private Handler handler;


    private Runnable delayedHide = () -> {
        postedHide = false;
        timeStart = -1;

        if (getActivity() != null) {

            try {
                dismiss();
            } catch (Exception e) {
                Log.e(TAG, "error dialog: ", e);
            }
        }
    };

    private final Runnable delayedShow = new Runnable() {

        @Override
        public void run() {
            postedShow = false;
            if (!dismissed) {
                timeStart = System.currentTimeMillis();
                try {
                    if (!isAdded()) {
                        show(fragmentManager, null);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "run: error dialog delay show", e);
                }
            }
        }
    };


    public static ProgressDialogMES getInstance(FragmentManager fragmentManager) {
        if (sInstance == null) {
            sInstance = new ProgressDialogMES();
            sInstance.init(fragmentManager);

        }

        if (fragmentManager != sInstance.fragmentManager) {
            sInstance.hide();
            sInstance.init(fragmentManager);
        }

        return sInstance;
    }

    private void init(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogProgressMES);

        if (dismissed) {
            try {
                dismiss();
            } catch (Exception e) {
                Log.e(TAG, "error dialog: ", e);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_progress_mes, null);
        RotativeImageView progressMES = layout.findViewById(R.id.progress_mes);

        progressMES.show();


        return layout;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removeCallbacks();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeCallbacks();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }


    private void removeCallbacks() {
        removeCallback(delayedHide);
        removeCallback(delayedShow);
    }

    private void removeCallback(Runnable runnable) {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    public void show() {
        // Reset the start time.
        timeStart = -1;
        dismissed = false;
        removeCallback(delayedHide);
        if (!postedShow) {
            postDelayed(delayedShow, MIN_DELAY);
            postedShow = true;
        }
    }


    public void hide() {

        if (dismissed) {
            return;
        }

        dismissed = true;

        long millisShowing = System.currentTimeMillis() - timeStart;
        if (millisShowing > MIN_SHOW_TIME || timeStart == -1) {
            try {
                if (getActivity() != null) {
                    dismiss();
                }
            } catch (Exception e) {
                Log.e(TAG, "hide progress dialog MES: ", e);
            }

        } else {
            postDelayed(delayedHide, MIN_SHOW_TIME - millisShowing);
        }

    }

    private void postDelayed(Runnable runnable, long timeStart) {
        handler.postDelayed(runnable, timeStart);
    }

    @Override
    public void onResume() {
        super.onResume();
        timeStart = System.currentTimeMillis();
    }

}

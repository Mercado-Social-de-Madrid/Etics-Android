package net.mercadosocial.moneda.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;

/**
 * Created by julio on 3/03/18.
 */

public class ProgressDialogMES extends DialogFragment {


    public static final long MIN_DELAY = 300;
    private static final long MIN_SHOW_TIME = 1000;

    private static final String TAG = "ProgressDialogMES";
    private static ProgressDialogMES progressInstance;
    private static ProgressDialogMES sInstance;

    private long timeStart = -1;
    boolean postedHide = false;
    boolean postedShow = false;
    boolean dismissed = false;

    private FragmentManager fragmentManager;
    private Handler handler;


    private Runnable delayedHide = new Runnable() {
        @Override
        public void run() {
            postedHide = false;
            timeStart = -1;
            if (getActivity() != null) {
                dismiss();
            }
        }
    };

    private final Runnable delayedShow = new Runnable() {

        @Override
        public void run() {
            postedShow = false;
            if (!dismissed) {
                timeStart = System.currentTimeMillis();
                show(fragmentManager, null);
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
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);

        if (dismissed) {
            dismiss();
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
        handler.removeCallbacks(runnable);
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
        dismissed = true;

        long millisShowing = System.currentTimeMillis() - timeStart;
        if (millisShowing > MIN_SHOW_TIME || timeStart == -1) {
            if (getActivity() != null) {
                dismiss();
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

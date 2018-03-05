package net.mercadosocial.moneda.views;

import android.app.Dialog;
import android.app.DialogFragment;
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


    public static final long TIME_START_THRESHOLD = 400;
    private static final long TIME_DURATION_SHOWING_MINIMUM = 1000;

    private static final String TAG = "ProgressDialogMES";
    private static ProgressDialogMES progressInstance;
    private long timeStart;
    private int numDialogs;
    private Handler handlerDialog;

    public static ProgressDialogMES newInstance() {
            return new ProgressDialogMES();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        timeStart = System.currentTimeMillis();
    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        progressInstance = null;
//    }

//    public void showSafe(FragmentManager manager) {
//
//        if (numDialogs == 0) {
////            startWithThresshold(manager);
//            show(manager, null);
//            timeStart = System.currentTimeMillis();
//        }
//
//        numDialogs++;
//    }
//
//    private void startWithThresshold(final FragmentManager manager) {
//
//            if (handlerDialog == null) {
//                handlerDialog = new Handler();
//                handlerDialog.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                }, TIME_START_THRESHOLD);
//            }
//    }

    public void dismissTimeSafe() {
        long millisShowing = System.currentTimeMillis() - timeStart;
        if (millisShowing > TIME_DURATION_SHOWING_MINIMUM) {
            dismiss();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, TIME_DURATION_SHOWING_MINIMUM - millisShowing);
        }

    }

//    public void dismissSafe() {
//        numDialogs--;
//        if (numDialogs == 0) {
//
//            if (handlerDialog != null) {
//                handlerDialog.removeCallbacksAndMessages(numDialogs);
//                handlerDialog = null;
//            }
//
//            dismissTimeSafe();
//        }
//    }

}

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

public class ProgressDialogMESOLD extends DialogFragment {


    public static final long MIN_DELAY = 300;
    private static final long MIN_SHOW_TIME = 1000;

    private static final String TAG = "ProgressDialogMES";
    private static ProgressDialogMESOLD progressInstance;
    private long timeStart;
    private int numDialogs;
    private Handler handlerDialog;
    private View tvClose;

    private boolean dismissed;

    public static ProgressDialogMESOLD newInstance() {
            return new ProgressDialogMESOLD();
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
//        tvClose = layout.findViewById(R.id.tv_close);
//
//        tvClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                Crashlytics.logException(new RuntimeException("Progress MES was closed by user"));
//            }
//        });

        progressMES.show();

        if (dismissed) {
            dismiss();
        }

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
//                }, MIN_DELAY);
//            }
//    }

    public void dismissTimeSafe() {
        dismissed = true;
        long millisShowing = System.currentTimeMillis() - timeStart;
        if (millisShowing > MIN_SHOW_TIME) {
            if (getActivity() != null) {
                dismiss();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        dismiss();
                    }
                }
            }, MIN_SHOW_TIME - millisShowing);
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

package net.mercadosocial.moneda.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by julio on 29/03/18.
 */

public class SoftKeyboardManager {
    private OnSoftKeyboardChangedListener listener;


    // -----

    public static SoftKeyboardManager newInstance() {
        return new SoftKeyboardManager();
    }

    public void configureSoftKeyboardVisibilityBehaviour(Activity activity, OnSoftKeyboardChangedListener listener) {
        configureSoftKeyboardVisibilityBehaviour(activity.getWindow().getDecorView().findViewById(android.R.id.content), listener);
    }

    public void configureSoftKeyboardVisibilityBehaviour(final View layout, final OnSoftKeyboardChangedListener listener) {
        this.listener = listener;

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                layout.getWindowVisibleDisplayFrame(r);
                int screenHeight = layout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

//                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    if (listener != null) {
                        listener.onSoftKeyboardVisible(true);
                    }
                } else {
                    if (listener != null) {
                        listener.onSoftKeyboardVisible(false);
                    }
                }
            }
        });
    }

    public interface OnSoftKeyboardChangedListener {
        void onSoftKeyboardVisible(boolean visible);
    }

    public void setOnSoftKeyboardChangedListener(OnSoftKeyboardChangedListener listener) {
        this.listener = listener;
    }

}

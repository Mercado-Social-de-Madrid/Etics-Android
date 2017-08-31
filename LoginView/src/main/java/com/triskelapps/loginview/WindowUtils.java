package com.triskelapps.loginview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

public final class WindowUtils {

    /**
     * Hides the soft keyword from the current activity.
     * 
     * @param activity The current activity
     */
    public static void hideSoftKeyword (Activity activity) {
        hideSoftKeyword(activity.getWindow());
    }

    /**
     * Hides the soft keyword from the current dialog.
     * 
     * @param dialog The current dialog
     */
    public static void hideSoftKeyword (Dialog dialog) {
        hideSoftKeyword(dialog.getWindow());
    }

    /**
     * Hides the soft keyword from the current activity.
     * 
     * @param window The current window
     */
    public static void hideSoftKeyword (Window window) {
        InputMethodManager imm =
            (InputMethodManager) window.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        View focus = window.getCurrentFocus();
        if (focus != null) {
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }
    
    public static void showSoftKeyboard (Context context, View view) {
    	InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

    	imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    private WindowUtils() {
        throw new AssertionError();
    }
}

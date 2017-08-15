package net.mercadosocial.moneda.util;

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
    public static void hideSoftKeyboard(Activity activity) {
        hideSoftKeyboard(activity.getWindow());
    }

    /**
     * Hides the soft keyword from the current dialog.
     * 
     * @param dialog The current dialog
     */
    public static void hideSoftKeyboard(Dialog dialog) {
        hideSoftKeyboard(dialog.getWindow());
    }

    /**
     * Hides the soft keyword from the current activity.
     * 
     * @param window The current window
     */
    public static void hideSoftKeyboard(Window window) {

        View view = window.getCurrentFocus();
        hideSoftKeyboard(view);
    }

    public static void hideSoftKeyboard(View view) {

        try {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

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

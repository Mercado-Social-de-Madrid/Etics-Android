package net.mercadosocial.moneda.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

/**
 * Created by julio on 11/07/16.
 */
public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQ_CODE = 324;

    private final Activity context;
    private final PermissionCallback callback;

    public PermissionHelper(@NonNull Activity context, @NonNull PermissionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    /**
     * Checks if the given permission has been granted by the user to this application before.
     *
     * @param permission to check availability for
     * @return true if the permission is currently granted, false otherwise.
     */
    public boolean isPermissionGranted(@NonNull String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PermissionChecker.PERMISSION_GRANTED;
    }

    /**
     * Starts the async request of the given permission.
     *
     * @param permission         to request to the user
     * @param messageExplanation optional message if should explain permission to user
     */
    public void requestPermission(@NonNull final String permission, @Nullable String messageExplanation) {

        if (isPermissionGranted(permission)) {
            callback.onPermissionGranted(permission);
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission) && messageExplanation != null) {
            //Show explanation dialog.
            //Optionally, call requestPermission again on positive button click

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(messageExplanation).setPositiveButton(context.getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, new String[]{permission}, PERMISSION_REQ_CODE);
                        }
                    }).setNegativeButton(context.getResources().getString(android.R.string.cancel), null).show();

        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{permission},
                    PERMISSION_REQ_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQ_CODE || (permissions.length == 0 && grantResults.length == 0)) {
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PermissionChecker.PERMISSION_GRANTED) {
                callback.onPermissionGranted(permissions[i]);
            } else {
                callback.onPermissionDenied(permissions[i]);
            }
        }
    }

    public interface PermissionCallback {
        /**
         * Called when a permission request is accepted by the user.
         *
         * @param permission the user just accepted.
         */
        void onPermissionGranted(String permission);

        /**
         * Called when a permission request is declined by the user.
         *
         * @param permission the user just declined.
         */
        void onPermissionDenied(String permission);
    }
}

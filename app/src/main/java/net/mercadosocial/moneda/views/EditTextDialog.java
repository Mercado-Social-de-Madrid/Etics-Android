package net.mercadosocial.moneda.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditTextDialog extends DialogFragment {

    private EditText mEditText;
    private OnDialogResultListener mListener;
    private String title;
    private int inputType = -1;

    public void setListener(OnDialogResultListener listener) {
        mListener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Creamos el EditText
        mEditText = new EditText(getActivity());

        if (inputType > -1) {
            mEditText.setInputType(inputType);
        } else {
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if (title != null) {
            builder.setTitle(title);
        }

        // Añadimos el EditText al diálogo
        builder.setView(mEditText);

        // Añadimos los botones "Aceptar" y "Cancelar"
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Al pulsar "Aceptar", devolvemos el valor introducido en el EditText
                String result = mEditText.getText().toString();
                if (mListener != null) {
                    mListener.onResult(result);
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);

        return builder.create();
    }

    public interface OnDialogResultListener {
        void onResult(String result);
    }
}

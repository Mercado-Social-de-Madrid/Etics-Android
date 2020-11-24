package net.mercadosocial.moneda.views;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.MES;

public class DialogSelectMES {

    private final Context context;
    private OnSelectMESListener onSelectMESListener;

    public DialogSelectMES(Context context) {
        this.context = context;
    }

    public static DialogSelectMES with(Context context) {
        return new DialogSelectMES(context);
    }

    public void show() {

        View layout = View.inflate(context, R.layout.view_dialog_change_social_market, null);
        SelectMESView selectMESView = (SelectMESView) layout.findViewById(R.id.select_mes_view);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(layout)
                .setNegativeButton(R.string.back, null)
                .show();

        String codeMESSaved = App.getPrefs(context).getString(App.SHARED_MES_CODE_SAVED, null);
        int positionSaved = MES.getMESPositionByCode(codeMESSaved);
        selectMESView.setSelectedMESPosition(positionSaved);
        selectMESView.setOnItemClickListener((view, position) -> {
            if (onSelectMESListener != null) {
                onSelectMESListener.onMESSelected(selectMESView.getSelectedMES());
            }

            dialog.dismiss();
        });


    }

    public DialogSelectMES setOnSelectMESListener(OnSelectMESListener listener) {
        this.onSelectMESListener = listener;
        return this;
    }
    public interface OnSelectMESListener {
        void onMESSelected(MES mes);
    }
}

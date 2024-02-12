package net.mercadosocial.moneda.views;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Node;

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

        Node node = ((App) context.getApplicationContext()).getCurrentNode();
        selectMESView.selectNode(node);
        selectMESView.setOnItemClickListener((view, position) -> {
            if (onSelectMESListener != null) {
                onSelectMESListener.onNodeSelected(selectMESView.getSelectedNode());
            }

            dialog.dismiss();
        });


    }

    public DialogSelectMES setOnSelectMESListener(OnSelectMESListener listener) {
        this.onSelectMESListener = listener;
        return this;
    }
    public interface OnSelectMESListener {
        void onNodeSelected(Node node);
    }
}

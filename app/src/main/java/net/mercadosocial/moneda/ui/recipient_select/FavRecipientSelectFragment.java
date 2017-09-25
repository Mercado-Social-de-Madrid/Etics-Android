package net.mercadosocial.moneda.ui.recipient_select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.mercadosocial.moneda.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by julio on 18/09/17.
 */

public class FavRecipientSelectFragment extends AppCompatDialogFragment {


    private List<String> names = new ArrayList<>();
    private OnFavouriteSelectedListener listener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_fav_recipient_select, null);

        final ListView listFavs = (ListView) layout.findViewById(R.id.list_favs);

        for (int i = 1; i < 21; i++) {
            names.add("Favorito " + i);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.grid_item_mock, names);

        listFavs.setAdapter(adapter);

        listFavs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = names.get(position);
                if (listener != null) {
                    listener.onFavouriteSelected(name);
                }
                dismiss();
            }
        });

        layout.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
    }

    public FavRecipientSelectFragment setOnFavouriteSelectedListener(OnFavouriteSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnFavouriteSelectedListener {
        void onFavouriteSelected(String name);
    }
}

package net.mercadosocial.moneda.ui.intro;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.views.SelectMESView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroLastItemFragment extends BaseFragment implements View.OnClickListener {
    
    private TextView btnIntroEnter;
    private SelectMESView selectMESView;

    private void findViews(View layout) {
        btnIntroEnter = (TextView)layout.findViewById( R.id.btn_intro_enter );
        selectMESView = (SelectMESView) layout.findViewById(R.id.select_mes_view);

        btnIntroEnter.setOnClickListener(this);
    }



    public IntroLastItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_intro_last_item, container, false);
        findViews(layout);

        Node node = ((App) getActivity().getApplicationContext()).getCurrentNode();
        selectMESView.selectNode(node);

        btnIntroEnter.setEnabled(true);

        return layout;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_intro_enter) {
            Node node = selectMESView.getSelectedNode();
            if (node != null) {
                ((App) getActivity().getApplicationContext()).setCurrentNode(node);
                getPrefs().edit().putBoolean(App.SHARED_INTRO_SEEN, true).apply();
                getActivity().finish();
            } else {
                toast(R.string.select_market);
            }
        }
    }
}

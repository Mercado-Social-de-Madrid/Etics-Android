package net.mercadosocial.moneda.ui.intro;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.MES;
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

        String codeMESSaved = getPrefs().getString(App.SHARED_MES_CODE_SAVED, null);
        int positionSaved = MES.getMESPositionByCode(codeMESSaved);
        selectMESView.setSelectedMESPosition(positionSaved);

        btnIntroEnter.setEnabled(true);

        return layout;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_intro_enter:

                MES mesSelected = selectMESView.getSelectedMES();
                getPrefs().edit().putString(App.SHARED_MES_CODE_SAVED, mesSelected.getCode()).commit();
                MES.setCityCode(mesSelected.getCode());

                getActivity().finish();
                break;
        }
    }
}

package net.mercadosocial.moneda.ui.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroLastItemFragment extends BaseFragment implements View.OnClickListener {
    
    private TextView btnIntroEnter;
    private TextView btnMembersInfo;

    private void findViews(View layout) {
        btnIntroEnter = (TextView)layout.findViewById( R.id.btn_intro_enter );
        btnMembersInfo = (TextView)layout.findViewById( R.id.btn_members_info );

        btnIntroEnter.setOnClickListener(this);
        btnMembersInfo.setOnClickListener(this);
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

        return layout;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_members_info:
//                startActivity();
            case R.id.btn_intro_enter:
                getActivity().finish();
                break;
        }
    }
}

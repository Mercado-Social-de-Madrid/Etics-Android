package net.mercadosocial.moneda.ui.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomIntroFragment extends SlideFragment {


    public CustomIntroFragment() {
        // Required empty public constructor
    }

    @Override
    public int buttonsColor() {
        return R.color.purple;
    }

    @Override
    public int backgroundColor() {
        return android.R.color.transparent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_intro, container, false);
    }

}

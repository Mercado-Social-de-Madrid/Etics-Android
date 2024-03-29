package net.mercadosocial.moneda.ui.intro;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroItemFragment extends BaseFragment {

    private static final String ARG_POSITION = "arg_position";

    private LinearLayout viewWhiteBg;
    private TextView tvIntroTitle;
    private TextView tvIntroText;
    private ImageView imgIntro;


    public static Fragment newInstance(int position) {
        IntroItemFragment introItemFragment = new IntroItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        introItemFragment.setArguments(args);
        return introItemFragment;
    }


    public IntroItemFragment() {
        // Required empty public constructor
    }
    

    private void findViews(View layout) {
        viewWhiteBg = layout.findViewById( R.id.view_white_bg );
        tvIntroTitle = layout.findViewById( R.id.tv_intro_title );
        tvIntroText = layout.findViewById( R.id.tv_intro_text );
        imgIntro = layout.findViewById(R.id.img_intro);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_intro_item, container, false);
        findViews(layout);

        int position = getArguments().getInt(ARG_POSITION);
        int titleId = getResources().getIdentifier("title_intro_" + position, "string", getActivity().getPackageName());
        int textId = getResources().getIdentifier("text_intro_" + position, "string", getActivity().getPackageName());
        int iconId  = getResources().getIdentifier("ic_intro_" + position, "mipmap", getActivity().getPackageName());

        checkValid(titleId, textId, iconId, position);

        tvIntroTitle.setText(titleId);
        tvIntroText.setText(textId);
        imgIntro.setImageResource(iconId);

        return layout;
    }

    private void checkValid(int titleId, int textId, int iconId, int position) {
        if(titleId == 0)
            throw new IllegalStateException("No intro title found for position " + position);

        if(textId == 0)
            throw new IllegalStateException("No intro text found for position " + position);

        if(iconId == 0)
            throw new IllegalStateException("No intro icon found for position " + position);
    }

}

package net.mercadosocial.moneda.ui.intro;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by julio on 17/01/18.
 */

public class IntroPagerAdapter extends FragmentPagerAdapter {

    public static final int NUM_INTRO_SCREENS = 7;

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == NUM_INTRO_SCREENS - 1) {
            return new IntroLastItemFragment();
        } else {
            return IntroItemFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return NUM_INTRO_SCREENS;
    }
}

package net.mercadosocial.moneda.ui.intro;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by julio on 17/01/18.
 */

public class IntroPagerAdapter extends FragmentPagerAdapter {

    public static final int NUM_INTRO_SCREENS = 8;

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new IntroFirstItemFragment();
        } else if (position == NUM_INTRO_SCREENS - 1) {
            return new IntroLastItemFragment();
        } else {
            return IntroItemFragment.newInstance(position - 1);
        }
    }

    @Override
    public int getCount() {
        return NUM_INTRO_SCREENS;
    }
}

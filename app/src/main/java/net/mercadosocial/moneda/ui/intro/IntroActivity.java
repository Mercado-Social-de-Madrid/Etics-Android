package net.mercadosocial.moneda.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

/**
 * Created by julio on 31/08/17.
 */

public class IntroActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    private IntroPagerAdapter adapter;
    private ViewPager viewPager;
    private ImageView btnIntroNext;

    private void findViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_intro);
        btnIntroNext = (ImageView) findViewById(R.id.btn_intro_next);

        viewPager.addOnPageChangeListener(this);
        btnIntroNext.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        findViews();

        adapter = new IntroPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new IntroTransformer());

        // todo transformer
        // todo font


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_intro_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
                break;
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        boolean visible = position < IntroPagerAdapter.NUM_INTRO_SCREENS - 1;
        btnIntroNext.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

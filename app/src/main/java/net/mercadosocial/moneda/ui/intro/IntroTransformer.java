package net.mercadosocial.moneda.ui.intro;

import android.view.View;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;

import net.mercadosocial.moneda.R;

/**
 * Created by julio on 17/10/17.
 */

public class IntroTransformer extends ABaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {

        View viewWhiteBg = view.findViewById(R.id.view_white_bg);

        if (viewWhiteBg == null) {
            return;
        }

        if (position <= 0f) {



            viewWhiteBg.setTranslationX(view.getWidth()*-position);
//            view.setScaleX(1f + position*1.3f);
//            view.setScaleY(1f);

            viewWhiteBg.setAlpha(1+position);

//            final float rotation = 90f * position;
//
////            view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
//            view.setPivotX(0);
//            view.setPivotY(view.getHeight() * 0.5f);
//            view.setRotationY(rotation);

        } else if (position <= 1f) {
//            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            viewWhiteBg.setAlpha(1 - position);
//            view.setPivotY(0.5f * view.getHeight());
            viewWhiteBg.setTranslationX(view.getWidth() * -position);
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
        }

    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }
}

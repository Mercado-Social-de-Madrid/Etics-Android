package net.mercadosocial.moneda.views;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class RotativeImageView extends AppCompatImageView {

    private boolean isShowing;
    private RotateAnimation rotateAnim;

    public RotativeImageView(Context context) {
        this(context, null);
    }

    public RotativeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotativeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        rotateAnim = new RotateAnimation(360.0f, 0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setInterpolator(new LinearInterpolator());
//        rotateAnim.setInterpolator(new CycleInterpolator(1f));
//        rotateAnim.setInterpolator(new LinearOutSlowInInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setDuration(1000);

        setVisibility(View.INVISIBLE);
        setAdjustViewBounds(true);
        isShowing = false;
    }


    public void show() {

        if(!isShowing) {

            setVisibility(View.VISIBLE);
            startAnimation(rotateAnim);
            isShowing = true;
        }
    }


    public void hide() {

        if(isShowing) {

            clearAnimation();
            setVisibility(View.INVISIBLE);
            isShowing = false;
        }
    }
}

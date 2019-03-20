package net.mercadosocial.moneda.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import net.mercadosocial.moneda.R;

public class ProgressView extends FrameLayout {

    private final View imgEticsRotative;
    private boolean isShowing;
    private RotateAnimation rotateAnim;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View layout = View.inflate(getContext(), R.layout.view_progress_etics, null);
        imgEticsRotative = layout.findViewById(R.id.img_mes_rotatitve);

        rotateAnim = new RotateAnimation(360.0f, 0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setInterpolator(new LinearInterpolator());
//        rotateAnim.setInterpolator(new LinearOutSlowInInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setDuration(3000);

        setVisibility(View.INVISIBLE);
        isShowing = false;

        show();
    }


    public void show() {

        if(!isShowing) {

            setVisibility(View.VISIBLE);
            imgEticsRotative.startAnimation(rotateAnim);
            isShowing = true;
        }
    }


    public void hide() {

        if(isShowing) {

            imgEticsRotative.clearAnimation();
            setVisibility(View.INVISIBLE);
            isShowing = false;
        }
    }
}

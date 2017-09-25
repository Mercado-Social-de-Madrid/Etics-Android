package net.mercadosocial.moneda.ui.intro;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

/**
 * Created by julio on 31/08/17.
 */

public class IntroActivity extends MaterialIntroActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setSkipButtonVisible();
        enableLastSlideAlphaExitTransition(true);


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.green_dark)
                .buttonsColor(R.color.purple)
                .image(R.mipmap.img_mes_header)
                .title("¡Bienvenid@!")
                .description(getString(R.string.intro_message_1))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.purple)
                .buttonsColor(R.color.green_dark)
                .title("Entidades")
                .description(getString(R.string.intro_message_2))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.green_dark)
                .buttonsColor(R.color.purple)
                .title("Novedades")
                .description(getString(R.string.intro_message_3))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.purple_dark)
                .buttonsColor(R.color.green_dark)
                .title("Pagos")
                .description(getString(R.string.intro_message_4))
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.purple)
                        .buttonsColor(R.color.green)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                        .title("Bonificaciones")
                        .description(getString(R.string.intro_message_5))
                        .build());

//        addSlide(new CustomIntroFragment());


    }

    @Override
    public void onFinish() {
        super.onFinish();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(App.SHARED_INTRO_SEEN, true).commit();
    }
}

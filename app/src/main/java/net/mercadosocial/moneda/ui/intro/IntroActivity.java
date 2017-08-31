package net.mercadosocial.moneda.ui.intro;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
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
                .title("Pantallas de introducción")
                .description("Ejemplo de cómo funcionan y cómo se pueden personalizar estas pantallas de introducción.\nPara pasar de diapositiva se puede deslizar el dedo o pulsar los botones inferiores")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.green)
                .buttonsColor(R.color.black)
                .title("Colores")
                .description("Se puede personalizar el color de fondo y el de los botones (el texto y las flechas de abajo van en blanco)")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.purple_light)
                .buttonsColor(R.color.green)
                .image(R.mipmap.img_mes_header)
                .title("Elementos")
                .description("En cada pantalla se puede escribir un título, descripción e imagen opcional")
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.purple)
                        .buttonsColor(R.color.green)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                        .title("Botón")
                        .description("También se puede poner un botón que realice alguna acción")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Haciendo algo...");
                    }
                }, "Hacer algo"));

        addSlide(new CustomIntroFragment());


    }

    @Override
    public void onFinish() {
        super.onFinish();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(App.SHARED_INTRO_SEEN, true).commit();
    }
}

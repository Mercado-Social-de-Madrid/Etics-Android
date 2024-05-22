package net.mercadosocial.moneda.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import net.mercadosocial.moneda.R;

import es.dmoral.toasty.Toasty;

public class WebUtils {


    public static boolean isValidLink(String link) {
        return link != null && Patterns.WEB_URL.matcher(link).matches();
    }

    public static void openLink(Context context, String url) {

        if (!isValidLink(url)) {
            Toasty.error(context, context.getString(R.string.invalid_link)).show();
            return;
        }

        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.toolbar))
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setDefaultColorSchemeParams(params)
                .build();

        try {
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (ActivityNotFoundException e1) {
                // this user has nothing to open webs?
            }
        }

    }
}

package net.mercadosocial.moneda.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EmojiGetter implements Html.ImageGetter {
    private Context context;
    private TextView textView;

    public EmojiGetter(Context context, TextView target) {
        this.context = context;
        textView = target;
    }

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder(context);
        Picasso.get()
                .load(source)
                .into(drawable);

        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        private final Context context;
        protected Drawable drawable;

        public BitmapDrawablePlaceHolder(Context context) {
            this.context = context;
        }

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            this.drawable = new BitmapDrawable(context.getResources(), bitmap);
            int size = (int) (textView.getTextSize() * 1.5f);
            drawable.setBounds(0, 0, size, size);
            setBounds(0, 0, size, size);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    }
}
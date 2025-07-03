package net.mercadosocial.moneda.ui.entity_info.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.mercadosocial.moneda.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GalleryFullScreenActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGES_SERIALIZED = "extra_images_serialized";
    private static final String EXTRA_START_IMAGE_POSITION = "extra_start_image_position";
    private String imagesSerialized;
    private int startPosition;
    private GalleryPagerFragment galleryPagerFragment;

    public static void launchGalleryFullScreen(Context context, String imagesSerialized, int startPosition) {
        Intent intent = new Intent(context, GalleryFullScreenActivity.class);
        intent.putExtra(EXTRA_IMAGES_SERIALIZED, imagesSerialized);
        intent.putExtra(EXTRA_START_IMAGE_POSITION, startPosition);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            EdgeToEdge.enable(this);
        }

        setContentView(R.layout.activity_gallery_full_screen);

        if (savedInstanceState != null) {
            imagesSerialized = savedInstanceState.getString(EXTRA_IMAGES_SERIALIZED);
            startPosition = savedInstanceState.getInt(EXTRA_START_IMAGE_POSITION, 0);
        } else {
            imagesSerialized = getIntent().getStringExtra(EXTRA_IMAGES_SERIALIZED);
            startPosition = getIntent().getIntExtra(EXTRA_START_IMAGE_POSITION, 0);
        }


        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> imagesUrls = new Gson().fromJson(imagesSerialized, listType);

        galleryPagerFragment = GalleryPagerFragment.newInstance(imagesUrls, startPosition);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_gallery_pager, galleryPagerFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_IMAGES_SERIALIZED, imagesSerialized);
        outState.putInt(EXTRA_START_IMAGE_POSITION, galleryPagerFragment.getCurrentPagerPosition());
        super.onSaveInstanceState(outState);
    }

}

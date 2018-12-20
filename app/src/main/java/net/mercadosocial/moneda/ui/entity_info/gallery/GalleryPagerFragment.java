package net.mercadosocial.moneda.ui.entity_info.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.mercadosocial.moneda.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryPagerFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String ARG_IMAGES_SERIALIZED = "arg_images_serialized";
    private static final String ARG_START_IMAGE_POSITION = "arg_start_image_position";

    private ViewPager viewpagerGallery;
    private ImageView imgArrowLeft;
    private ImageView imgArrowRight;
    private List<String> imagesUrls;

    private void findViews(View layout) {
        viewpagerGallery = (ViewPager) layout.findViewById(R.id.viewpager_gallery);
        imgArrowLeft = (ImageView) layout.findViewById(R.id.img_arrow_left);
        imgArrowRight = (ImageView) layout.findViewById(R.id.img_arrow_right);

        imgArrowLeft.setOnClickListener(this);
        imgArrowRight.setOnClickListener(this);
    }

    public static GalleryPagerFragment newInstance(List<String> imagesUrls, int startImagePosition) {
        GalleryPagerFragment galleryPagerFragment = new GalleryPagerFragment();
        String serialized = new Gson().toJson(imagesUrls);
        Bundle args = new Bundle();
        args.putString(ARG_IMAGES_SERIALIZED, serialized);
        args.putInt(ARG_START_IMAGE_POSITION, startImagePosition);
        galleryPagerFragment.setArguments(args);
        return galleryPagerFragment;
    }

    public GalleryPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_gallery_pager, container, false);
        findViews(layout);

        final String imagesSerialized = getArguments().getString(ARG_IMAGES_SERIALIZED);
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        imagesUrls = new Gson().fromJson(imagesSerialized, listType);

        GalleryPagerAdapter adapter = new GalleryPagerAdapter(getActivity().getSupportFragmentManager(), imagesUrls);
        if (!(getActivity() instanceof GalleryFullScreenActivity)) {
            adapter.setOnImageClickListener(new GalleryPagerAdapter.onImageClickListener() {
                @Override
                public void onImageClick(int position) {
                    GalleryFullScreenActivity.launchGalleryFullScreen(getActivity(), imagesSerialized, position);
                }
            });
        }

        viewpagerGallery.setAdapter(adapter);
        viewpagerGallery.addOnPageChangeListener(this);

        int startImagePosition = getArguments().getInt(ARG_START_IMAGE_POSITION);
        viewpagerGallery.setCurrentItem(startImagePosition);

        updateArrowsVisibility();

        return layout;
    }

    // INTERACTIONS
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateArrowsVisibility();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_arrow_left:
                viewpagerGallery.setCurrentItem(viewpagerGallery.getCurrentItem() - 1, true);
                break;

            case R.id.img_arrow_right:
                viewpagerGallery.setCurrentItem(viewpagerGallery.getCurrentItem() + 1, true);
                break;

        }
    }


    private void updateArrowsVisibility() {

        if (imagesUrls.isEmpty()) {
            imgArrowLeft.setVisibility(View.GONE);
            imgArrowRight.setVisibility(View.GONE);
            return;
        }

        imgArrowLeft.setVisibility(viewpagerGallery.getCurrentItem() == 0 ? View.GONE : View.VISIBLE);
        imgArrowRight.setVisibility(viewpagerGallery.getCurrentItem() == imagesUrls.size() - 1 ? View.GONE : View.VISIBLE);
    }


    public int getCurrentPagerPosition() {
        return viewpagerGallery.getCurrentItem();
    }
}

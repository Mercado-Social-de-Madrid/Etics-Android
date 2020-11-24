package net.mercadosocial.moneda.ui.entity_info.gallery;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by julio on 2/03/18.
 */


public class GalleryPagerAdapter extends FragmentPagerAdapter {


    private final List<String> imagesUrls;
    private onImageClickListener listener;

    public GalleryPagerAdapter(FragmentManager fm, List<String> imagesUrls) {
        super(fm);
        this.imagesUrls = imagesUrls;
    }

    public void setOnImageClickListener(onImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Fragment getItem(final int position) {
        GalleryItemFragment fragment = GalleryItemFragment.newInstance(imagesUrls.get(position));
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            }
        });
        return fragment;

    }

    @Override
    public int getCount() {
        return imagesUrls.size();
    }

    public interface onImageClickListener {
        void onImageClick(int position);
    }

}

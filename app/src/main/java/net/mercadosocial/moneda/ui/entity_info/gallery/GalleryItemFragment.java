package net.mercadosocial.moneda.ui.entity_info.gallery;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryItemFragment extends Fragment {


    private static final java.lang.String ARG_IMAGE_URL = "arg_image_url";
    private View.OnClickListener onClickListener;

    public GalleryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_gallery_item, container, false);

        ImageView imageView = layout.findViewById(R.id.img_gallery_item);

        String imageUrl = getArguments().getString(ARG_IMAGE_URL);

        Picasso.get()
                .load(imageUrl)
                .error(R.mipmap.img_mes_default_banner_2)
                .into(imageView);

        imageView.setOnClickListener(onClickListener);

        return layout;

    }

    public static GalleryItemFragment newInstance(String imageUrl) {

        GalleryItemFragment fragment = new GalleryItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

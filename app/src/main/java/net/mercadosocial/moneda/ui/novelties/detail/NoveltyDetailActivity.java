package net.mercadosocial.moneda.ui.novelties.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.News;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.model.Offer;
import net.mercadosocial.moneda.util.Util;

import es.dmoral.toasty.Toasty;

public class NoveltyDetailActivity extends BaseActivity implements NoveltyDetailView {

    private NoveltyDetailPresenter presenter;
    private TextView tvNoveltyTitle;
    private TextView tvNoveltySubtitle;
    private ImageView imgNovelty;
    private TextView tvNoveltyDescription;
    private TextView btnMoreInfo;

    private void findViews() {
        tvNoveltyTitle = (TextView)findViewById( R.id.tv_novelty_title );
        tvNoveltySubtitle = (TextView)findViewById( R.id.tv_novelty_subtitle );
        imgNovelty = (ImageView)findViewById( R.id.img_novelty );
        tvNoveltyDescription = (TextView)findViewById( R.id.tv_novelty_description );
        btnMoreInfo = (TextView)findViewById( R.id.btn_more_info );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NoveltyDetailPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novelty_detail);
        findViews();
        configureSecondLevelActivity();


        presenter.onCreate(getIntent().getExtras());
    }



    // PRESENTER CALLBACKS
    @Override
    public void showOffer(Offer offer) {
        setTitle(R.string.offer);
        showNovelty(offer);

    }


    @Override
    public void showNews(final News news) {
        setTitle(R.string.news_mes);
        showNovelty(news);

        if (news.getMore_info_text() != null) {
            btnMoreInfo.setVisibility(View.VISIBLE);
            btnMoreInfo.setText(news.getMore_info_text());
            btnMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getMore_info_url())));
                    } catch (ActivityNotFoundException e) {
                        Toasty.warning(NoveltyDetailActivity.this, getString(R.string.invalid_link)).show();
                    }
                }
            });
        }
    }

    private void showNovelty(Novelty novelty) {
        tvNoveltyTitle.setText(novelty.getTitleNovelty());
        tvNoveltySubtitle.setText(novelty.getSubtitleNovelty());

        String image = novelty.getImageNoveltyUrl();

        if (Util.isValidLink(image)) {
            imgNovelty.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(image)
                    .placeholder(novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.mipmap.img_mes_header : R.mipmap.ic_offer_semitransp)
                    .resizeDimen(R.dimen.width_image_standard, R.dimen.height_image_small)
                    .into(imgNovelty);
        } else {
            imgNovelty.setVisibility(View.GONE);
        }

        Util.setHtmlLinkableText(tvNoveltyDescription, novelty.getDescriptionFullNovelty());

    }
}

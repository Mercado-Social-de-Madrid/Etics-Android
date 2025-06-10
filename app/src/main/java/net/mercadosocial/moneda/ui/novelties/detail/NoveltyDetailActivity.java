package net.mercadosocial.moneda.ui.novelties.detail;

import android.os.Bundle;
import android.view.View;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.databinding.ActivityNoveltyDetailBinding;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.News;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.model.Offer;
import net.mercadosocial.moneda.ui.entity_info.EntityInfoPresenter;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.util.WebUtils;

public class NoveltyDetailActivity extends BaseActivity implements NoveltyDetailView {

    private NoveltyDetailPresenter presenter;
    private ActivityNoveltyDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NoveltyDetailPresenter.newInstance(this, this);
        setBasePresenter(presenter);
        super.onCreate(savedInstanceState);
        binding = ActivityNoveltyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSecondLevelActivity();

        presenter.onCreate(getIntent().getExtras());
    }



    // PRESENTER CALLBACKS
    @Override
    public void showOffer(Offer offer) {
        setToolbarTitle(R.string.offer);
        showNovelty(offer);

    }


    @Override
    public void showNews(final News news) {
        setToolbarTitle(R.string.news_mes);
        showNovelty(news);

        if (news.getMore_info_text() != null) {
            binding.btnMoreInfo.setVisibility(View.VISIBLE);
            binding.btnMoreInfo.setText(news.getMore_info_text());
            binding.btnMoreInfo.setOnClickListener(v -> WebUtils.openLink(NoveltyDetailActivity.this, news.getMore_info_url()));
        }
    }

    private void showNovelty(Novelty novelty) {
        binding.tvNoveltyTitle.setText(novelty.getTitleNovelty());


        switch (novelty.getNoveltyType()) {
            case Novelty.TYPE_NEWS:
                binding.tvNoveltyDate.setText(getString(R.string.published, novelty.getDate()));
                break;

            case Novelty.TYPE_OFFER:

                Entity entity = ((Offer) novelty).getEntity();
                if (entity != null) {
                    binding.tvNoveltyEntity.setText(entity.getName());
                    binding.tvNoveltyEntity.setVisibility(View.VISIBLE);
                    binding.tvNoveltyEntity.setOnClickListener(v -> {
                        EntityInfoPresenter.startEntityInfoActivity(NoveltyDetailActivity.this, entity.getId());
                    });
                }

                binding.tvNoveltyDate.setText(getString(R.string.valid_until, novelty.getDate()));
                break;

            default:
                throw new IllegalStateException("Novelty type don't exists: " + novelty.getNoveltyType());

        }

        String image = novelty.getImageNoveltyUrl();

        if (WebUtils.isValidLink(image)) {
            binding.imgNovelty.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(image)
                    .placeholder(novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.mipmap.img_mes_header : R.mipmap.ic_offer_semitransp)
                    .resizeDimen(R.dimen.width_image_standard, R.dimen.height_image_small)
                    .into(binding.imgNovelty);
        } else {
            binding.imgNovelty.setVisibility(View.GONE);
        }

        Util.setHtmlLinkableText(binding.tvNoveltyDescription, novelty.getDescriptionFullNovelty());

    }
}

package net.mercadosocial.moneda.ui.entity_info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.databinding.ActivityEntityInfoBinding;
import net.mercadosocial.moneda.model.Benefit;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entity_info.gallery.GalleryPagerFragment;
import net.mercadosocial.moneda.util.Util;

public class EntityInfoActivity extends BaseActivity implements View.OnClickListener, EntityInfoView, EntitiyOffersAdapter.OnItemClickListener {

    private EntityInfoPresenter presenter;
    private EntitiyOffersAdapter adapter;
    private ActivityEntityInfoBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = EntityInfoPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        binding = ActivityEntityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSecondLevelActivity();

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerOffers.addItemDecoration(itemDecoration);

        binding.btnRrssWeb.setOnClickListener(this);
        binding.btnRrssTelegram.setOnClickListener(this);
        binding.btnRrssTwitter.setOnClickListener(this);
        binding.btnRrssFacebook.setOnClickListener(this);
        binding.btnRrssInstagram.setOnClickListener(this);

        presenter.onCreate(getIntent());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_rrss_web:
            case R.id.btn_rrss_telegram:
            case R.id.btn_rrss_twitter:
            case R.id.btn_rrss_facebook:
            case R.id.btn_rrss_instagram:
                Util.openLink(this, String.valueOf(v.getTag()));
                break;
        }
    }

    @Override
    public void showEntityInfo(Entity entity) {

        binding.tvEntityName.setText(entity.getName());
        if (entity.getDescription() != null) {
            Util.setHtmlLinkableText(binding.tvEntityDescription, entity.getDescription());
        }

        binding.tvNoOffers.setVisibility(
                entity.getOffers() == null || entity.getOffers().isEmpty() ? View.VISIBLE : View.GONE);

        Picasso.get()
                .load(entity.getLogo())
//                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_mes_header)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(binding.imgEntity);

        if (adapter == null) {
            adapter = new EntitiyOffersAdapter(this, entity.getOffers());
            adapter.setOnItemClickListener(this);
            binding.recyclerOffers.setAdapter(adapter);
        } else {
            adapter.updateData(entity.getOffers());
        }

        GalleryPagerFragment galleryPagerFragment = GalleryPagerFragment.newInstance(entity.getGalleryImages(), 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_gallery_pager, galleryPagerFragment).commit();

        setupRRSSButtons(entity);

        binding.viewDistintivo.setVisibility(entity.getBalance_url() != null ? View.VISIBLE : View.GONE);
        binding.imgDistintivoBalance.setOnClickListener(v -> {
            if (entity.getBalance_url() != null) {
                Util.openLink(this, entity.getBalance_url());
            }
        });

    }

    @Override
    public void showBenefitsInfo(Benefit benefit, boolean isEntity, String noBenefitText) {

        if (noBenefitText != null) {
            binding.tvMembersBenefits.setText(noBenefitText);
            binding.tvBenefitInPerson.setVisibility(View.GONE);
            binding.tvBenefitOnline.setVisibility(View.GONE);
            binding.btnBenefitLink.setVisibility(View.GONE);
            binding.tvBenefitCode.setVisibility(View.GONE);
            return;
        }

        binding.tvMembersBenefits.setText(benefit.getBenefitText(isEntity));

        binding.tvBenefitInPerson.setCompoundDrawablesWithIntrinsicBounds(
                benefit.isInPerson() ? R.mipmap.ic_green_check : R.mipmap.ic_red_cross, 0, 0, 0);

        binding.tvBenefitOnline.setCompoundDrawablesWithIntrinsicBounds(
                benefit.isOnline() ? R.mipmap.ic_green_check : R.mipmap.ic_red_cross, 0, 0, 0);

        binding.tvBenefitCode.setVisibility(TextUtils.isEmpty(benefit.getDiscountCode()) ? View.GONE : View.VISIBLE);
        binding.tvBenefitCode.setText(getString(R.string.benefit_code_x, benefit.getDiscountCode()));

        if (isEntity) {
            binding.btnBenefitLink.setVisibility(TextUtils.isEmpty(benefit.getDiscountLinkEntities()) ? View.GONE : View.VISIBLE);
            binding.btnBenefitLink.setOnClickListener(v -> presenter.onBenefitLinkClick(benefit.getDiscountLinkEntities()));
        } else {
            binding.btnBenefitLink.setVisibility(TextUtils.isEmpty(benefit.getDiscountLinkMembers()) ? View.GONE : View.VISIBLE);
            binding.btnBenefitLink.setOnClickListener(v -> presenter.onBenefitLinkClick(benefit.getDiscountLinkMembers()));
        }

        binding.btnBenefitLink.setText(benefit.getDiscountLinkText());

    }

    private void setupRRSSButtons(Entity entity) {

        binding.btnRrssWeb.setVisibility(Util.isValidLink(entity.getWebpage_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssWeb.setTag(entity.getWebpage_link());

        binding.btnRrssTelegram.setVisibility(Util.isValidLink(entity.getTelegram_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssTelegram.setTag(entity.getTelegram_link());

        binding.btnRrssTwitter.setVisibility(Util.isValidLink(entity.getTwitter_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssTwitter.setTag(entity.getTwitter_link());

        binding.btnRrssFacebook.setVisibility(Util.isValidLink(entity.getFacebook_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssFacebook.setTag(entity.getFacebook_link());

        binding.btnRrssInstagram.setVisibility(Util.isValidLink(entity.getInstagram_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssInstagram.setTag(entity.getInstagram_link());

    }

    @Override
    public void onOfferClicked(String id, int position) {

        presenter.onOfferClicked(position);
    }

}

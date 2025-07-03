package net.mercadosocial.moneda.ui.entity_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.databinding.ActivityEntityInfoBinding;
import net.mercadosocial.moneda.model.Benefit;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entity_info.gallery.GalleryPagerFragment;
import net.mercadosocial.moneda.util.MatcherUtil;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.util.WebUtils;
import net.mercadosocial.moneda.views.CircleTransform;

import java.util.Locale;

public class EntityInfoActivity extends BaseActivity implements EntityInfoView, EntitiyOffersAdapter.OnItemClickListener {

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

        setToolbarTitle(R.string.entity_information);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerOffers.addItemDecoration(itemDecoration);

        presenter.onCreate(getIntent());

    }


    @Override
    public void showEntityInfo(Entity entity) {

        binding.tvEntityName.setText(entity.getName());
        Util.setHtmlLinkableText(binding.tvEntityDescription, entity.getDescription());


        binding.tvNoOffers.setVisibility(
                entity.getOffers() == null || entity.getOffers().isEmpty() ? View.VISIBLE : View.GONE);

        if (entity.getProfileImage() != null) {
            Picasso.get()
                    .load(entity.getProfileImage())
//                .placeholder(R.mipmap.img_default_grid)
                    .error(R.mipmap.img_mes_header)
                    .transform(new CircleTransform())
                    .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                    .into(binding.imgLogoEntity);
        } else {
            binding.imgLogoEntity.setVisibility(View.GONE);
        }

        if (adapter == null) {
            adapter = new EntitiyOffersAdapter(this, entity.getOffers());
            adapter.setOnItemClickListener(this);
            binding.recyclerOffers.setAdapter(adapter);
        } else {
            adapter.updateData(entity.getOffers());
        }

        GalleryPagerFragment galleryPagerFragment = GalleryPagerFragment.newInstance(entity.getGalleryImages(), 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_gallery_pager, galleryPagerFragment).commit();

        setupInfoButtons(entity);
        setupRRSSButtons(entity);

        binding.viewDistintivo.setVisibility(entity.getBalanceUrl() != null ? View.VISIBLE : View.GONE);
        binding.imgDistintivoBalance.setOnClickListener(v -> {
            if (entity.getBalanceUrl() != null) {
                WebUtils.openLink(this, entity.getBalanceUrl());
            }
        });

        binding.tvEntityShortDescription.setVisibility(TextUtils.isEmpty(entity.getShort_description()) ? View.GONE : View.VISIBLE);
        binding.tvEntityShortDescription.setText(entity.getShort_description());

        binding.tvTitleServices.setVisibility(TextUtils.isEmpty(entity.getServices()) ? View.GONE : View.VISIBLE);
        binding.tvEntityServices.setVisibility(TextUtils.isEmpty(entity.getServices()) ? View.GONE : View.VISIBLE);
        Util.setHtmlLinkableText(binding.tvEntityServices, entity.getServices());

        String categoriesStr = entity.getCategoriesString();
        binding.tvCategories.setText(categoriesStr);

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

    private void setupInfoButtons(Entity entity) {

        binding.btnRrssWeb.setVisibility(WebUtils.isValidLink(entity.getWebpage_link()) ? View.VISIBLE : View.GONE);
        binding.btnRrssWeb.setOnClickListener(v -> WebUtils.openLink(this, entity.getWebpage_link()));

        binding.btnRrssEmail.setVisibility(MatcherUtil.isValidEmail(entity.getEmail()) ? View.VISIBLE : View.GONE);
        binding.btnRrssEmail.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", entity.getEmail(), null))));

        binding.btnRrssPhone.setVisibility(MatcherUtil.isValidPhone(entity.getPhone_number()) ? View.VISIBLE : View.GONE);
        binding.btnRrssPhone.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(
                        "tel", entity.getPhone_number(), null))));

        binding.btnRrssMap.setVisibility(entity.getLatitude() != 0 && entity.getLongitude() != 0 ? View.VISIBLE : View.GONE);
        binding.btnRrssMap.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(Locale.UK, "https://maps.google.com/?q=%f,%f", entity.getLatitude(), entity.getLongitude())))));

    }

    private void setupRRSSButtons(Entity entity) {

        if (entity.getSocialProfiles() != null) {
            ProviderSocialProfileAdapter socialProfileAdapter =
                    new ProviderSocialProfileAdapter(this, entity.getSocialProfiles());

            socialProfileAdapter.setOnItemClickListener((view, position) -> {
                String url = entity.getSocialProfiles().get(position).getUrl();
                WebUtils.openLink(this, url);
            });

            binding.recyclerSocialProfiles.setAdapter(socialProfileAdapter);
        }

    }

    @Override
    public void setBalanceBadge(String balanceBadge) {
        Picasso.get()
                .load(balanceBadge)
                .error(R.mipmap.img_distntivo_balance)
                .into(binding.imgDistintivoBalance);

    }

    @Override
    public void onOfferClicked(String id, int position) {

        presenter.onOfferClicked(position);
    }

}

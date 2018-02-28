package net.mercadosocial.moneda.ui.entity_info;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.util.Util;

import es.dmoral.toasty.Toasty;

public class EntityInfoActivity extends BaseActivity implements View.OnClickListener, EntityInfoView, EntitiyOffersAdapter.OnItemClickListener {

    private ImageView imgEntity;
    private ImageView imgHeart;
    private TextView tvAcceptBoniatos;
    private TextView tvBonusBoniatos;
    private TextView btnNewPayment;
    private TextView tvEntityDescription;
    private RecyclerView recyclerOffers;
    private TextView tvEntityName;
    private EntityInfoPresenter presenter;
    private EntitiyOffersAdapter adapter;


    private void findViews() {
        imgEntity = (ImageView)findViewById( R.id.img_entity );
        imgHeart = (ImageView)findViewById( R.id.img_heart );
        tvAcceptBoniatos = (TextView)findViewById( R.id.tv_accept_boniatos );
        tvBonusBoniatos = (TextView)findViewById( R.id.tv_bonus_boniatos );
        btnNewPayment = (TextView)findViewById( R.id.btn_new_payment );
        tvEntityDescription = (TextView)findViewById( R.id.tv_entity_description );
        recyclerOffers = (RecyclerView)findViewById( R.id.recycler_offers );
        tvEntityName = (TextView)findViewById( R.id.tv_entity_name );

        btnNewPayment.setOnClickListener(this);
        imgHeart.setOnClickListener(this);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = EntityInfoPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_info);

        findViews();
        configureSecondLevelActivity();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerOffers.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerOffers.addItemDecoration(itemDecoration);

        presenter.onCreate(getIntent());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_payment:
                presenter.onNewPaymentClick();
                break;

            case R.id.img_heart:
                Toasty.info(this, getString(R.string.social_balance_info_soon)).show();
                break;
        }
    }

    @Override
    public void showEntityInfo(Entity entity) {

        tvEntityName.setText(entity.getName());
        if (entity.getDescription() != null) {
            Util.setHtmlLinkableText(tvEntityDescription, entity.getDescription());
        }

        tvAcceptBoniatos.setText(entity.getMax_percent_payment() + "%");
        tvBonusBoniatos.setText(entity.getBonusPercent(this) + "%");

        Picasso.with(this)
                .load(entity.getLogoFullUrl())
//                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_mes_header)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(imgEntity);

        if (adapter == null) {
            adapter = new EntitiyOffersAdapter(this, entity.getOffers());
            adapter.setOnItemClickListener(this);
            recyclerOffers.setAdapter(adapter);
        } else {
            adapter.updateData(entity.getOffers());
        }
    }

    @Override
    public void hidePaymentButton() {
        btnNewPayment.setVisibility(View.GONE);
    }

    @Override
    public void onOfferClicked(String id, int position) {

        presenter.onOfferClicked(position);
    }

}

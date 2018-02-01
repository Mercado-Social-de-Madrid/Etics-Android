package net.mercadosocial.moneda.ui.entity_info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.payment_old.NewPaymentActivity_Old;

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

        presenter.onCreate(getIntent());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_payment:
                startActivity(NewPaymentActivity_Old.newNewPaymentActivity(this, -1));
                break;
        }
    }

    @Override
    public void showEntityInfo(Entity entity) {

        tvEntityName.setText(entity.getName());
        tvEntityDescription.setText(entity.getDescription());

//        Picasso.with(this)
//                .load(entity.getLogo())
////                .placeholder(R.mipmap.img_default_grid)
//                .error(R.mipmap.img_mes_header)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
//                .into(imgEntity);

        if (adapter == null) {
            adapter = new EntitiyOffersAdapter(this, entity.getOffers());
            adapter.setOnItemClickListener(this);
            recyclerOffers.setAdapter(adapter);
        } else {
            adapter.updateData(entity.getOffers());
        }
    }

    @Override
    public void onOfferClicked(String id, int position) {

        presenter.onOfferClicked(position);
    }

}

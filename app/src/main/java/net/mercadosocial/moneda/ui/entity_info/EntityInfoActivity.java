package net.mercadosocial.moneda.ui.entity_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.payment.NewPaymentActivity;

public class EntityInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";
    private static final String EXTRA_ENTITY = "extra_entity";

    private TextView tvEntityName;
    private ImageView imgEntity;
    private TextView tvEntityDescription;
    private View btnNewPayment;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    public static Intent newEntityInfoActivity(Context context, Entity entity) {
        Intent intent = new Intent(context, EntityInfoActivity.class);
        intent.putExtra(EXTRA_ENTITY, entity);
        return intent;
    }

    private void findViews() {
        tvEntityName = (TextView) findViewById(R.id.tv_entity_name);
        imgEntity = (ImageView) findViewById(R.id.img_entity);
        tvEntityDescription = (TextView) findViewById(R.id.tv_entity_description);
        btnNewPayment = findViewById(R.id.btn_new_payment);

        btnNewPayment.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_info);

        findViews();
        configureSecondLevelActivity();

        Entity entity = (Entity) getIntent().getSerializableExtra(EXTRA_ENTITY);

        tvEntityName.setText(entity.getName());
        tvEntityDescription.setText(entity.getDescription());

        Picasso.with(this)
                .load(entity.getLogo())
//                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_mes_header)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(imgEntity);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_payment:
                startActivity(NewPaymentActivity.newNewPaymentActivity(this, -1));
                break;
        }
    }
}

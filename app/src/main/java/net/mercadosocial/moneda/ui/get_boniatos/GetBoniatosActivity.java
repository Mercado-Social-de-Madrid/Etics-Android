package net.mercadosocial.moneda.ui.get_boniatos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

public class GetBoniatosActivity extends BaseActivity implements GetBoniatosView, View.OnClickListener {

    private GetBoniatosPresenter presenter;
    private EditText editGetBoniatosAmount;
    private AppCompatButton btnRecharge;

    private void findViews() {
        editGetBoniatosAmount = (EditText)findViewById( R.id.edit_get_boniatos_amount );
        btnRecharge = (AppCompatButton)findViewById( R.id.btn_recharge );

        btnRecharge.setOnClickListener( this );
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = GetBoniatosPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_boniatos);
        findViews();
        configureSecondLevelActivity();


    }

    @Override
    public void onClick(View v) {
        if ( v == btnRecharge ) {

            launchDialog();
        }
    }

    private void launchDialog() {

        setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, 3000);
    }


}

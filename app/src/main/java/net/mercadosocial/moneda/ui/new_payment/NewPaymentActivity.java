package net.mercadosocial.moneda.ui.new_payment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;

public class NewPaymentActivity extends BaseActivity implements NewPaymentView, View.OnClickListener {

    private NewPaymentPresenter presenter;
    private LinearLayout viewProgressPayment;
    private TextView tvStep1;
    private TextView tvStep2;
    private TextView tvStep3;
    private FrameLayout framePayment;
    private TextView btnContinue;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    private void findViews() {
        viewProgressPayment = (LinearLayout)findViewById( R.id.view_progress_payment );
        tvStep1 = (TextView)findViewById( R.id.tv_step_1 );
        tvStep2 = (TextView)findViewById( R.id.tv_step_2 );
        tvStep3 = (TextView)findViewById( R.id.tv_step_3 );
//        framePayment = (FrameLayout)findViewById( R.id.frame_payment );
        btnContinue = (TextView)findViewById( R.id.btn_continue );

        btnContinue.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NewPaymentPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        findViews();
        configureSecondLevelActivity();


//        getFragmentManager().beginTransaction().add(R.id.frame_payment, new NewPaymentStep1Fragment()).commit();


        presenter.onCreate(getIntent());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:

                break;
        }
    }
}

package net.mercadosocial.moneda.ui.new_payment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.new_payment.step1.NewPaymentStep1Fragment;
import net.mercadosocial.moneda.ui.new_payment.step2.NewPaymentStep2Fragment;
import net.mercadosocial.moneda.ui.new_payment.step3.NewPaymentStep3Fragment;

public class NewPaymentActivity extends BaseActivity implements NewPaymentView, View.OnClickListener {

    private NewPaymentPresenter presenter;
    private LinearLayout viewProgressPayment;
    private TextView tvStep1;
    private TextView tvStep2;
    private TextView tvStep3;
    private FrameLayout framePaymentSection;
    private NewPaymentStep1Fragment fragmentStep1;
    private NewPaymentStep2Fragment fragmentStep2;
    private NewPaymentStep3Fragment fragmentStep3;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    private void findViews() {
        viewProgressPayment = (LinearLayout)findViewById( R.id.view_progress_payment );
        tvStep1 = (TextView)findViewById( R.id.tv_step_1 );
        tvStep2 = (TextView)findViewById( R.id.tv_step_2 );
        tvStep3 = (TextView)findViewById( R.id.tv_step_3 );
        framePaymentSection = (FrameLayout)findViewById( R.id.frame_payment_section );

        tvStep1.setOnClickListener(this);
        tvStep2.setOnClickListener(this);
        tvStep3.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NewPaymentPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        findViews();
        configureSecondLevelActivity();


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragmentStep1 = new NewPaymentStep1Fragment();
        fragmentStep2 = new NewPaymentStep2Fragment();
        fragmentStep3 = new NewPaymentStep3Fragment();

        ft.add(R.id.frame_payment_section, fragmentStep1);
        ft.add(R.id.frame_payment_section, fragmentStep2);
        ft.add(R.id.frame_payment_section, fragmentStep3);

        ft.hide(fragmentStep2);
        ft.hide(fragmentStep3);

        ft.commit();


        presenter.onCreate(getIntent());

    }


    public void showSection(int step) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (step) {
            case 1:
                ft.show(fragmentStep1);
                ft.hide(fragmentStep2);
                ft.hide(fragmentStep3);
                break;

            case 2:
                ft.hide(fragmentStep1);
                ft.show(fragmentStep2);
                ft.hide(fragmentStep3);
                break;

            case 3:
                ft.hide(fragmentStep1);
                ft.hide(fragmentStep2);
                ft.show(fragmentStep3);
                break;
        }

        ft.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_step_1:
                showSection(1);
                break;

            case R.id.tv_step_2:
                showSection(2);
                break;

            case R.id.tv_step_3:
                showSection(3);
                break;
        }
    }


}

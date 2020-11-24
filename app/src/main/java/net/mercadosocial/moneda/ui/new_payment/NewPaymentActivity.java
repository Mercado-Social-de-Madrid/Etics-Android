package net.mercadosocial.moneda.ui.new_payment;


import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.ui.new_payment.step1.NewPaymentStep1Fragment;
import net.mercadosocial.moneda.ui.new_payment.step2.NewPaymentStep2Fragment;
import net.mercadosocial.moneda.ui.new_payment.step3.NewPaymentStep3Fragment;
import net.mercadosocial.moneda.ui.new_payment.step3.NewPaymentStep3Presenter;
import net.mercadosocial.moneda.util.WindowUtils;

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
    private View[] stepsViews;


    private void findViews() {
        viewProgressPayment = (LinearLayout)findViewById( R.id.view_progress_payment );
        tvStep1 = (TextView)findViewById( R.id.tv_step_1 );
        tvStep2 = (TextView)findViewById( R.id.tv_step_2 );
        tvStep3 = (TextView)findViewById( R.id.tv_step_3 );
        framePaymentSection = (FrameLayout)findViewById( R.id.frame_payment_section );

        stepsViews = new View[]{tvStep1, tvStep2, tvStep3};

//        tvStep1.setOnClickListener(this);
//        tvStep2.setOnClickListener(this);
//        tvStep3.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NewPaymentPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        findViews();
        configureSecondLevelActivity();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentStep1 = new NewPaymentStep1Fragment();
        fragmentStep2 = new NewPaymentStep2Fragment();
        fragmentStep3 = new NewPaymentStep3Fragment();

        ft.add(R.id.frame_payment_section, fragmentStep1);
        ft.add(R.id.frame_payment_section, fragmentStep2);
        ft.add(R.id.frame_payment_section, fragmentStep3);

        ft.hide(fragmentStep1);
        ft.hide(fragmentStep2);
        ft.hide(fragmentStep3);

        ft.commit();


        presenter.onCreate(getIntent());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSection(int step) {

        WindowUtils.hideSoftKeyboard(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
                ((NewPaymentStep3Presenter)fragmentStep3.getBasePresenter()).refreshData();
                break;
        }

        ft.commit();

        selectIndicator(step);
    }

    @Override
    public void onQRScanned(String url) {
            fragmentStep1.onQRScanned(url);
    }

    @Override
    public void showTitle(String title) {
        setToolbarTitle(title);
    }

    private void selectIndicator(int step) {
        for (int i = 0; i < stepsViews.length; i++) {
            View stepsView = stepsViews[i];
            stepsView.setSelected(i < step);
        }
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_step_1:
            case R.id.tv_step_2:
            case R.id.tv_step_3:
                presenter.onIndicatorSectionClick(Integer.parseInt(v.getTag().toString()));
                break;
        }
    }


}

package net.mercadosocial.moneda.ui.profile.pincode_change;


import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.WalletInteractor;

import es.dmoral.toasty.Toasty;

public class PincodeChangePresenter extends BasePresenter {

    private final PincodeChangeView view;

    public static void launchPincodeChangeActivity(Context context) {

        Intent intent = new Intent(context, PincodeChangeActivity.class);

        context.startActivity(intent);
    }

    public static PincodeChangePresenter newInstance(PincodeChangeView view, Context context) {

        return new PincodeChangePresenter(view, context);

    }

    private PincodeChangePresenter(PincodeChangeView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onChangePincodeClick(String pincode, String password) {

        try {
            Integer.parseInt(pincode);
        } catch (NumberFormatException e) {
            Toasty.error(context, getString(R.string.invalid_code)).show();
        }

        view.showProgressDialog(getString(R.string.sending));
        new WalletInteractor(context, view).resetPincode(pincode, password, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(context, getString(R.string.success2)).show();
                finish();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });
    }

}
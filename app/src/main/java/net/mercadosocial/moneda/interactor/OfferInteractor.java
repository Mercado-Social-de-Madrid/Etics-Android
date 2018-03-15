package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.OffersApi;
import net.mercadosocial.moneda.api.response.OffersResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Offer;
import net.mercadosocial.moneda.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class OfferInteractor extends BaseInteractor {
    

    public OfferInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getOffers(final BaseApiGETListCallback<Offer> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getOffers()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<OffersResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(OffersResponse offersResponse) {

                        baseView.setRefreshing(false);

                        callback.onResponse(offersResponse.getOffers());


                    }
                });


    }


    private OffersApi getApi() {
        return getApi(OffersApi.class);
    }


}

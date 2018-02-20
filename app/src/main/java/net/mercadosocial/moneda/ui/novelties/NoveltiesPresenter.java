package net.mercadosocial.moneda.ui.novelties;

import android.content.Context;

import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.OfferInteractor;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.model.Offer;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 20/02/18.
 */


public class NoveltiesPresenter extends BasePresenter {

    private final NoveltiesView view;
    private final OfferInteractor offerInteractor;
    private List<Novelty> novelties = new ArrayList<>();

    public static NoveltiesPresenter newInstance(NoveltiesView view, Context context) {

        return new NoveltiesPresenter(view, context);

    }

    private NoveltiesPresenter(NoveltiesView view, Context context) {
        super(context, view);

        this.view = view;
        offerInteractor = new OfferInteractor(context, view);

    }

    public void onCreate() {

        refreshData();
    }

    public void onResume() {

    }

    public void refreshData() {

        novelties.clear();
        view.showProgressDialog("Cargando...");

        offerInteractor.getOffers(new BaseInteractor.BaseApiGETListCallback<Offer>() {
            @Override
            public void onResponse(List<Offer> offers) {
                addOffersToNovelties(offers);
                // todo mix with news
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });

    }

    private void addOffersToNovelties(List<Offer> offers) {

        for (Offer offer : offers) {
            novelties.add(offer);
        }

        view.showNovelties(novelties);
    }

}

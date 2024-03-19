package net.mercadosocial.moneda.ui.novelties.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.NewsInteractor;
import net.mercadosocial.moneda.interactor.OfferInteractor;
import net.mercadosocial.moneda.model.News;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.model.Offer;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 21/02/18.
 */


public class NoveltyDetailPresenter extends BasePresenter {

    private static final String EXTRA_OFFER = "extra_offer";
    private static final String EXTRA_NEWS = "extra_news";
    private static final String EXTRA_NEWS_ID = "extra_news_id";
    private static final String EXTRA_OFFER_ID = "extra_offer_id";

    public enum NOVELTY_TYPE {
        OFFER,
        NEWS,
        OTHER,
    }


    private final NoveltyDetailView view;

    public static Intent newNoveltyDetailActivity(Context context, Novelty novelty) {
        Intent intent = new Intent(context, NoveltyDetailActivity.class);
        if (novelty instanceof Offer) {
            intent.putExtra(EXTRA_OFFER, (Offer) novelty);
        } else if (novelty instanceof News) {
            intent.putExtra(EXTRA_NEWS, (News) novelty);
        }
        return intent;
    }

    public static Intent newNoveltyDetailActivity(Context context, NOVELTY_TYPE type, String noveltyId) {
        Intent intent = new Intent(context, NoveltyDetailActivity.class);
        switch (type) {
            case OFFER:
                intent.putExtra(EXTRA_OFFER_ID, noveltyId);
                break;
            case NEWS:
                intent.putExtra(EXTRA_NEWS_ID, noveltyId);
                break;
        }
        return intent;
    }

    public static NoveltyDetailPresenter newInstance(NoveltyDetailView view, Context context) {

        return new NoveltyDetailPresenter(view, context);

    }

    private NoveltyDetailPresenter(NoveltyDetailView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onCreate(Bundle extras) {

        if (extras.containsKey(EXTRA_OFFER)) {
            view.showOffer((Offer) extras.get(EXTRA_OFFER));
        } else if (extras.containsKey(EXTRA_NEWS)) {
            view.showNews((News) extras.get(EXTRA_NEWS));
        } else if (extras.containsKey(EXTRA_NEWS_ID)) {
            getNewsById(extras.getString(EXTRA_NEWS_ID));
        } else if (extras.containsKey(EXTRA_OFFER_ID)) {
            getOfferById(extras.getString(EXTRA_OFFER_ID));
        } else {
            throw new IllegalStateException("Extras invalid in Novelty detail");
        }

    }

    private void getNewsById(String idNews) {

        new NewsInteractor(context, view).getNewsById(idNews, new BaseInteractor.BaseApiCallback<News>() {
            @Override
            public void onResponse(News news) {
                view.showNews(news);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, context.getString(R.string.error_retrieving_data)).show();
            }
        });
    }

    private void getOfferById(String offerId) {

        new OfferInteractor(context, view).getOfferById(offerId, new BaseInteractor.BaseApiCallback<Offer>() {
            @Override
            public void onResponse(Offer offer) {
                view.showOffer(offer);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, context.getString(R.string.error_retrieving_data)).show();
            }
        });
    }


    public void onResume() {

        refreshData();
    }

    public void refreshData() {


    }

}

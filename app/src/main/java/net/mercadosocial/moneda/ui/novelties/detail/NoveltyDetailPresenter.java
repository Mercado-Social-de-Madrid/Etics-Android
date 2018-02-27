package net.mercadosocial.moneda.ui.novelties.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.News;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.model.Offer;

/**
 * Created by julio on 21/02/18.
 */



 public class NoveltyDetailPresenter extends BasePresenter {

    private static final String EXTRA_OFFER = "extra_offer";
    private static final String EXTRA_NEWS = "extra_news";

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
         } else {
             throw new IllegalStateException("Extras invalid in Novelty detail");
         }

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }

 }

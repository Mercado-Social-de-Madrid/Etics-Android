package net.mercadosocial.moneda.ui.novelties.detail;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.News;
import net.mercadosocial.moneda.model.Offer;

/**
 * Created by julio on 21/02/18.
 */

public interface NoveltyDetailView extends BaseView {
    void showOffer(Offer offer);

    void showNews(News news);
}

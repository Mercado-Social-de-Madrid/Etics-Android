package net.mercadosocial.moneda.ui.novelties;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Novelty;

import java.util.List;

/**
 * Created by julio on 20/02/18.
 */

public interface NoveltiesView extends BaseView {

    void showNovelties(List<Novelty> novelties);

}

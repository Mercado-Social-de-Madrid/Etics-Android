package net.mercadosocial.moneda.ui.main;

import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Node;

/**
 * Created by julio on 2/02/18.
 */

public interface MainView extends BaseView {

    void showUserData(Data userData);

    void showNodeSocialProfiles(Node node);
}

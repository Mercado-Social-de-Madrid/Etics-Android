package net.mercadosocial.moneda.ui.fediverse.list;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.FediversePost;

import java.util.List;

public interface FediverseView extends BaseView {
    void showPosts(List<FediversePost> posts);
}

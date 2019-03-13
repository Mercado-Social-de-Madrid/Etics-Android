package net.mercadosocial.moneda.ui.profile;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

public interface ProfileView extends BaseView {
    void showPersonProfile(Person person);

    void showEntityInfo(Entity entity);
}

package net.mercadosocial.moneda.ui.auth.register;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.model.User;

/**
 * Created by julio on 16/02/18.
 */

public interface RegisterView extends BaseView {

    void showRegisterPerson();

    void showRegisterEntity();

    void setContinueRegisterEnable(boolean enable);

    void fillUserData(User user);

    void fillPersonData(Person person);

    void fillEntityData(Entity entity);
}

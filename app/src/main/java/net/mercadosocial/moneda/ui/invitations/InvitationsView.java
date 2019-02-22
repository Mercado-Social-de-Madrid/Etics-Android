package net.mercadosocial.moneda.ui.invitations;

import net.mercadosocial.moneda.base.BaseView;

public interface InvitationsView extends BaseView {

    void configureInviteButton(String text, boolean enabled);

    void clearInvitationEmailText();
    void showAvailableInvitations(int numInvitations);
}

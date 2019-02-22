package net.mercadosocial.moneda.ui.invitations;


import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;

public class InvitationsPresenter extends BasePresenter {

    private final InvitationsView view;
    private final UserInteractor userInteractor;

    public static void launchInvitationsActivity(Context context) {

        Intent intent = new Intent(context, InvitationsActivity.class);

        context.startActivity(intent);
    }

    public static InvitationsPresenter newInstance(InvitationsView view, Context context) {

        return new InvitationsPresenter(view, context);

    }

    private InvitationsPresenter(InvitationsView view, Context context) {
        super(context, view);

        this.view = view;
        userInteractor = new UserInteractor(context, view);

    }

    public void onCreate() {

    }

    public void onResume() {

        refreshData();
    }

    public void refreshData() {


    }

    public void sendInvitation(String emailInvitation) {

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInvitation).matches()) {
            view.toast(R.string.invalid_email);
            return;
        }

        view.configureInviteButton(context.getString(R.string.sending), false);
        userInteractor.sendInvitation(emailInvitation, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                view.toast(R.string.invitation_sent);
                view.configureInviteButton(context.getString(R.string.invite), true);
                view.clearInvitationEmailText();
            }

            @Override
            public void onError(String message) {
                view.toast(message);
                view.configureInviteButton(context.getString(R.string.invite), true);
            }
        });
    }
}

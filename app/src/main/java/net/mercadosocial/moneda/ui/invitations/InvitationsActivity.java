package net.mercadosocial.moneda.ui.invitations;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.util.WindowUtils;

public class InvitationsActivity extends BaseActivity implements InvitationsView, View.OnClickListener {

    private InvitationsPresenter presenter;
    private EditText editEmailInvitation;
    private Button btnInvite;

    private void findViews() {

        editEmailInvitation = findViewById(R.id.edit_email_invitation);
        btnInvite = findViewById(R.id.btn_invite);

        btnInvite.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = InvitationsPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        findViews();
        configureSecondLevelActivity();

        setToolbarTitle(R.string.invitations);



    }


    // INTERACTIONS
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_invite:
                String emailInvitation = editEmailInvitation.getText().toString();
                presenter.sendInvitation(emailInvitation);
                WindowUtils.hideSoftKeyboard(this);
                break;
        }
    }

    // PRESENTER CALLBACKS

    @Override
    public void configureInviteButton(String text, boolean enabled) {

        btnInvite.setEnabled(enabled);
        btnInvite.setText(text);
    }

    @Override
    public void clearInvitationEmailText() {
        editEmailInvitation.setText("");
    }

    @Override
    public void showAvailableInvitations(int numInvitations) {

    }

}

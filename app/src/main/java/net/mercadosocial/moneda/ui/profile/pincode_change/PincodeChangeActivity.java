package net.mercadosocial.moneda.ui.profile.pincode_change;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

public class PincodeChangeActivity extends BaseActivity implements View.OnClickListener, PincodeChangeView {

    private PinEntryEditText editPinEntry;
    private EditText editPassword;
    private AppCompatButton btnSave;
    private PincodeChangePresenter presenter;

    private void findViews() {
        editPinEntry = (PinEntryEditText) findViewById(R.id.edit_pin_entry);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnSave = (AppCompatButton) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = PincodeChangePresenter.newInstance(this, this);
        setBasePresenter(presenter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode_change);
        findViews();
        configureSecondLevelActivity();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String pincode = editPinEntry.getText().toString();
                String password = editPassword.getText().toString();
                presenter.onChangePincodeClick(pincode, password);
                break;
        }
    }
}

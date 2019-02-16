package net.mercadosocial.moneda.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.ui.main.MainActivity;

public class BlockActivity extends BaseActivity {

    private static final String SHARED_VALID_TEST_CODE = "shared_valid_test_code";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        EditText editTestCode = (EditText) findViewById(R.id.edit_test_code);

        findViewById(R.id.tv_block_text).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mercadosocial.net/"))));

        findViewById(R.id.btn_enter_test_code).setOnClickListener(v -> {
            String testCode = getString(R.string.test_code_string);
            String inputCode = editTestCode.getText().toString();
            if (TextUtils.equals(testCode, inputCode)) {
                enterMainActivity();
                getPrefs().edit().putBoolean(SHARED_VALID_TEST_CODE, true).commit();
            } else {
                toast(R.string.invalid_code);
//                editTestCode.setError(getString(R.string.invalid_code));
            }
        });

        if (getPrefs().getBoolean(SHARED_VALID_TEST_CODE, false)) {
            enterMainActivity();
        }
    }

    private void enterMainActivity() {
        startActivity(new Intent(BlockActivity.this, MainActivity.class));
        finish();
    }
}

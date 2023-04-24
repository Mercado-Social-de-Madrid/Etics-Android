package net.mercadosocial.moneda.ui.info;

import android.os.Bundle;

import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.databinding.ActivityInfoMesBinding;

public class InfoMesActivity extends BaseActivity {

    private ActivityInfoMesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoMesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnWhereToConsume.setOnClickListener( v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}

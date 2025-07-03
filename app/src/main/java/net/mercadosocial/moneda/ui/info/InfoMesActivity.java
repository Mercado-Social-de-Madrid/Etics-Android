package net.mercadosocial.moneda.ui.info;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.databinding.ActivityInfoMesBinding;
import net.mercadosocial.moneda.model.Node;

public class InfoMesActivity extends BaseActivity {

    private ActivityInfoMesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInfoMesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Node node = getApp().getCurrentNode();
        if (node.getInfoPageUrl() != null) {
            binding.webviewInfoMes.loadUrl(node.getInfoPageUrl());
        }

        binding.btnWhereToConsume.setOnClickListener( v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

}

package com.br.ecommerce.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityRecuperaContaBinding;

public class RecuperaContaActivity extends AppCompatActivity {

    private ActivityRecuperaContaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperaContaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.include.ibVoltar.setOnClickListener(view -> finish());
    }
}
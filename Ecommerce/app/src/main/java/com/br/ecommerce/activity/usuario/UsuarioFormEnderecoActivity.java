package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityUsuarioEnderecoBinding;

public class UsuarioFormEnderecoActivity extends AppCompatActivity {

    private ActivityUsuarioEnderecoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
        binding.include.textTitulo.setText("Novo endereço");
    }
}
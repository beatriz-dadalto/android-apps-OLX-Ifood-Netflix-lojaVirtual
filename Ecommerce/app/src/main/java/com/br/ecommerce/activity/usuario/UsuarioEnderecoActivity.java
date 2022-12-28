package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.databinding.ActivityUsuarioEnderecoBinding;

public class UsuarioEnderecoActivity extends AppCompatActivity {

    private ActivityUsuarioEnderecoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUsuarioEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.include.textTitulo.setText("Meus Endereços");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());
    }
}
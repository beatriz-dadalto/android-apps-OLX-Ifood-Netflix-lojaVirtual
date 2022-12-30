package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityUsuarioSelecionaEnderecoBinding;

public class UsuarioSelecionaEnderecoActivity extends AppCompatActivity {

    private ActivityUsuarioSelecionaEnderecoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioSelecionaEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        binding.include.textTitulo.setText("Endereço de entrega");
    }
}
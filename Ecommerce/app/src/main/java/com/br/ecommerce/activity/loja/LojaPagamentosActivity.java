package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityLojaPagamentosBinding;

public class LojaPagamentosActivity extends AppCompatActivity {

    private ActivityLojaPagamentosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaPagamentosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
    }

    private void configCliques() {
        binding.include.btnAdd.setOnClickListener(view ->
                startActivity(new Intent(this, LojaFormPagamentoActivity.class)));
    }
    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Formas de pagamentos");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());
    }
}
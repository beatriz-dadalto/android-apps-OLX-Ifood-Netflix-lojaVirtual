package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.LojaPagamentoAdapter;
import com.br.ecommerce.databinding.ActivityLojaPagamentosBinding;
import com.br.ecommerce.model.FormaPagamento;

import java.util.ArrayList;
import java.util.List;

public class LojaPagamentosActivity extends AppCompatActivity implements LojaPagamentoAdapter.OnClick {

    private ActivityLojaPagamentosBinding binding;
    private LojaPagamentoAdapter lojaPagamentoAdapter;
    private List<FormaPagamento> formaPagamentoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaPagamentosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
        configRv();
    }

    private void configCliques() {
        binding.include.btnAdd.setOnClickListener(view ->
                startActivity(new Intent(this, LojaFormPagamentoActivity.class)));
    }

    private void configRv() {
        binding.rvPagamentos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPagamentos.setHasFixedSize(true);
        lojaPagamentoAdapter = new LojaPagamentoAdapter(formaPagamentoList, this, this);
        binding.rvPagamentos.setAdapter(lojaPagamentoAdapter);
    }

    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Formas de pagamentos");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());
    }

    @Override
    public void onClickListener(FormaPagamento formaPagamento) {

    }
}
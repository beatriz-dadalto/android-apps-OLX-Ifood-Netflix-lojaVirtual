package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.UsuarioPagamentoAdapter;
import com.br.ecommerce.databinding.ActivityUsuarioSelecionaPagamentoBinding;
import com.br.ecommerce.model.FormaPagamento;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSelecionaPagamentoActivity extends AppCompatActivity implements UsuarioPagamentoAdapter.OnClick {

    private ActivityUsuarioSelecionaPagamentoBinding binding;
    private UsuarioPagamentoAdapter usuarioPagamentoAdapter;
    private List<FormaPagamento> formaPagamentoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioSelecionaPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
        configRv();
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
    }

    private void configRv() {
        binding.rvPagamentos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPagamentos.setHasFixedSize(true);
        usuarioPagamentoAdapter = new UsuarioPagamentoAdapter(formaPagamentoList, this);
        binding.rvPagamentos.setAdapter(usuarioPagamentoAdapter);
    }

    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Forma de pagamento");
    }

    @Override
    public void onClickListener(FormaPagamento formaPagamento) {

    }
}
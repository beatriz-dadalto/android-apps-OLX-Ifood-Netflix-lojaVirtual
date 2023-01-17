package com.br.ecommerce.activity.loja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.LojaPagamentoAdapter;
import com.br.ecommerce.databinding.ActivityLojaPagamentosBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.FormaPagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
        recuperaFormaPagamento();
    }

    private void configCliques() {
        binding.include.btnAdd.setOnClickListener(view ->
                startActivity(new Intent(this, LojaFormPagamentoActivity.class)));
    }
    private void recuperaFormaPagamento() {
        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
                .child("formapagamento");
        pagamentoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    formaPagamentoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FormaPagamento formaPagamento = ds.getValue(FormaPagamento.class);
                        formaPagamentoList.add(formaPagamento);
                    }
                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhuma forma de pagamento cadastrada");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(formaPagamentoList);
                lojaPagamentoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        Intent intent = new Intent(this, LojaFormPagamentoActivity.class);
        intent.putExtra("formaPagamentoSelecionada", formaPagamento);
        startActivity(intent);
    }
}
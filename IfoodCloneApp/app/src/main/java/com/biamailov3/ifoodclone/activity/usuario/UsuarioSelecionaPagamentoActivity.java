package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.SelecionaEnderecoAdapter;
import com.biamailov3.ifoodclone.adapter.SelecionaPagamentoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.Pagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioSelecionaPagamentoActivity extends AppCompatActivity implements SelecionaPagamentoAdapter.OnClickListener {

    private List<Pagamento> pagamentoList = new ArrayList<>();
    private SelecionaPagamentoAdapter selecionaPagamentoAdapter;

    private RecyclerView rvPagamentos;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_seleciona_pagamento);

        inciarComponentes();
        configCliques();
        configRv();
        recuperarPagamentos();
    }

    private void recuperarPagamentos() {
        DatabaseReference pagamentosRef = FirebaseHelper.getDatabaseReference()
                .child("recebimentos")
                .child(FirebaseHelper.getIdFirebase());
        pagamentosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pagamento pagamento = ds.getValue(Pagamento.class);
                        pagamentoList.add(pagamento);
                    }

                } else {
                    textInfo.setText("Nenhuma forma de pagamento habilitada");
                }

                progressBar.setVisibility(View.GONE);
                selecionaPagamentoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvPagamentos.setLayoutManager(new LinearLayoutManager(this));
        rvPagamentos.setHasFixedSize(true);
        selecionaPagamentoAdapter = new SelecionaPagamentoAdapter(pagamentoList, this);
        rvPagamentos.setAdapter(selecionaPagamentoAdapter);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void inciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Formas de pagamentos");

        rvPagamentos = findViewById(R.id.rv_pagamentos);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
    }

    @Override
    public void onClick(Pagamento pagamento) {

    }
}
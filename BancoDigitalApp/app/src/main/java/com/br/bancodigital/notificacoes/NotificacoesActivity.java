package com.br.bancodigital.notificacoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.bancodigital.R;
import com.br.bancodigital.adapter.NotificacaoAdapter;
import com.br.bancodigital.cobranca.PagamentoCobrancaActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Notificacao;
import com.br.bancodigital.transferencia.TransferenciaReciboActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificacoesActivity extends AppCompatActivity implements NotificacaoAdapter.OnClick {

    private List<Notificacao> notificacaoList = new ArrayList<>();
    private NotificacaoAdapter notificacaoAdapter;

    private RecyclerView rvNotificacoes;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        configToolbar();
        iniciaComponente();
        configRv();
        recuperaNotificacoes();
    }

    private void configRv(){
        rvNotificacoes.setLayoutManager(new LinearLayoutManager(this));
        rvNotificacoes.setHasFixedSize(true);
        notificacaoAdapter = new NotificacaoAdapter(notificacaoList, getBaseContext(), this);
        rvNotificacoes.setAdapter(notificacaoAdapter);
    }

    private void recuperaNotificacoes() {
        DatabaseReference notificacoesRef = FirebaseHelper.getDatabaseReference()
                .child("notificacoes")
                .child(FirebaseHelper.getIdFirebase());
        notificacoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    notificacaoList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Notificacao notificacao = ds.getValue(Notificacao.class);
                        notificacaoList.add(notificacao);
                    }

                    textInfo.setText("");
                } else {
                    textInfo.setText("Você tem nenhuma notificação.");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(notificacaoList);
                notificacaoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Notificações");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponente() {
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
        rvNotificacoes = findViewById(R.id.rvNotificacoes);
    }

    @Override
    public void onClickListener(Notificacao notificacao) {

        if (notificacao.getOperacao().equals("COBRANCA")) {
            Intent intent = new Intent(this, PagamentoCobrancaActivity.class);
            intent.putExtra("notificacao", notificacao);
            startActivity(intent);
        } else if (notificacao.getOperacao().equals("TRANSFERENCIA")) {
            Intent intent = new Intent(this, TransferenciaReciboActivity.class);
            intent.putExtra("idTransferencia", notificacao.getIdOperacao());
            startActivity(intent);
        } else {

        }

    }
}
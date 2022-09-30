package com.br.bancodigital.cobranca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bancodigital.R;
import com.br.bancodigital.app.MainActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Cobranca;
import com.br.bancodigital.model.Extrato;
import com.br.bancodigital.model.Notificacao;
import com.br.bancodigital.model.Pagamento;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PagamentoCobrancaActivity extends AppCompatActivity {

    private TextView textValor;
    private TextView textData;
    private TextView textUsuario;
    private ImageView imagemUsuario;
    private ProgressBar progressBar;

    private AlertDialog dialog;

    private Cobranca cobranca;
    private Notificacao notificacao;
    private Usuario usuarioDestino;
    private Usuario usuarioOrigem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_cobranca);

        configToolbar();
        iniciaComponentes();
        recuperaUsuarioOrigem();
        getExtra();

    }

    public void confirmarPagamento(View view) {
        if (cobranca != null) {
            if (!cobranca.isPaga()) {
                if (usuarioDestino != null && usuarioOrigem != null) {
                    if (usuarioOrigem.getSaldo() >= cobranca.getValor()) {

                        usuarioOrigem.setSaldo(usuarioOrigem.getSaldo() - cobranca.getValor());
                        usuarioOrigem.atualizarSaldo();

                        usuarioDestino.setSaldo(usuarioDestino.getSaldo() + cobranca.getValor());
                        usuarioDestino.atualizarSaldo();

                        atualizarStatusCobranca();

                        // enviou o pagamento
                        salvarExtrato(usuarioOrigem, "SAIDA");

                        // recebeu o pagamento
                        salvarExtrato(usuarioDestino, "ENTRADA");

                    } else {
                        showDialog("Saldo insuficiente.");
                    }
                } else {
                    Toast.makeText(this, "Ainda estamos recuperando as informações.", Toast.LENGTH_SHORT).show();
                }
            } else {
                showDialog("O pagamento já foi realizado para esta cobrança.");
            }
        } else {
            Toast.makeText(this, "Ainda estamos recuperando as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarExtrato(Usuario usuario, String tipo) {

        Extrato extrato = new Extrato();
        extrato.setOperacao("PAGAMENTO");
        extrato.setValor(cobranca.getValor());
        extrato.setTipo(tipo);

        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference()
                .child("extratos")
                .child(usuario.getId())
                .child(extrato.getId());
        extratoRef.setValue(extrato).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateExtrato = extratoRef
                        .child("data");
                updateExtrato.setValue(ServerValue.TIMESTAMP);

                salvarPagamento(extrato);

            } else {
                showDialog("Não foi possível efetuar o pagamento, tente mais tarde.");
            }
        });

    }

    private void salvarPagamento(Extrato extrato) {

        Pagamento pagamento = new Pagamento();
        pagamento.setId(extrato.getId());
        pagamento.setIdCobranca(cobranca.getId());
        pagamento.setValor(cobranca.getValor());
        pagamento.setIdUserDestino(usuarioDestino.getId());
        pagamento.setIdUserOrigem(usuarioOrigem.getId());

        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
                .child("pagamentos")
                .child(extrato.getId());
        pagamentoRef.setValue(pagamento).addOnSuccessListener(aVoid -> {

            DatabaseReference update = pagamentoRef.child("data");
            update.setValue(ServerValue.TIMESTAMP);

        });

        if (extrato.getTipo().equals("ENTRADA")) {
            configNotificacao(extrato.getId());
        } else {
            Intent intent = new Intent(this, ReciboPagamentoActivity.class);
            intent.putExtra("idPagamento", pagamento.getId());
            startActivity(intent);
        }

    }

    private void atualizarStatusCobranca() {
        DatabaseReference cobrancaRef = FirebaseHelper.getDatabaseReference()
                .child("cobrancas")
                .child(FirebaseHelper.getIdFirebase())
                .child(notificacao.getIdOperacao())
                .child("paga");
        cobrancaRef.setValue(true);
    }

    private void getExtra() {
        notificacao = (Notificacao) getIntent().getSerializableExtra("notificacao");

        recuperaCobranca();
    }

    private void recuperaCobranca() {
        DatabaseReference cobrancaRef = FirebaseHelper.getDatabaseReference()
                .child("cobrancas")
                .child(FirebaseHelper.getIdFirebase())
                .child(notificacao.getIdOperacao());
        cobrancaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cobranca = snapshot.getValue(Cobranca.class);

                recuperaUsuarioDestino();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configNotificacao(String idOperacao) {
        Notificacao notificacao = new Notificacao();
        notificacao.setIdOperacao(idOperacao);
        notificacao.setIdDestinatario(usuarioDestino.getId());
        notificacao.setIdEmitente(usuarioOrigem.getId());
        notificacao.setOperacao("PAGAMENTO");

        // Envia a notificação para o usuário que irá receber a cobrança
        enviarNotificacao(notificacao);

    }

    // Envia a notificação para o usuário que irá receber o pagamento
    private void enviarNotificacao(Notificacao notificacao) {
        DatabaseReference noficacaoRef = FirebaseHelper.getDatabaseReference()
                .child("notificacoes")
                .child(notificacao.getIdDestinatario())
                .child(notificacao.getId());
        noficacaoRef.setValue(notificacao).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateRef = noficacaoRef
                        .child("data");
                updateRef.setValue(ServerValue.TIMESTAMP);

            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this, R.style.CustomAlertDialog
        );

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        dialog = builder.create();
        dialog.show();

    }

    private void recuperaUsuarioDestino() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(cobranca.getIdEmitente());
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioDestino = snapshot.getValue(Usuario.class);
                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaUsuarioOrigem() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioOrigem = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        textUsuario.setText(usuarioDestino.getNome());
        if (usuarioDestino.getUrlImagem() != null) {
            Picasso.get().load(usuarioDestino.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .into(imagemUsuario);
        }

        textData.setText(GetMask.getDate(cobranca.getData(), 3));
        textValor.setText(getString(R.string.text_valor, GetMask.getValor(cobranca.getValor())));

    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Confirme os dados");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes() {
        textValor = findViewById(R.id.textValor);
        textData = findViewById(R.id.textData);
        textUsuario = findViewById(R.id.textUsuario);
        imagemUsuario = findViewById(R.id.imagemUsuario);
        progressBar = findViewById(R.id.progressBar);
    }

}
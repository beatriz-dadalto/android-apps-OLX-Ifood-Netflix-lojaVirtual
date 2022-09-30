package com.br.bancodigital.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bancodigital.R;
import com.br.bancodigital.adapter.ExtratoAdapter;
import com.br.bancodigital.autenticacao.LoginActivity;
import com.br.bancodigital.cobranca.CobrancaFormActivity;
import com.br.bancodigital.deposito.DepositoFormActivity;
import com.br.bancodigital.extrato.ExtratoActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Extrato;
import com.br.bancodigital.model.Notificacao;
import com.br.bancodigital.model.Usuario;
import com.br.bancodigital.notificacoes.NotificacoesActivity;
import com.br.bancodigital.recarga.RecargaFormActivity;
import com.br.bancodigital.transferencia.TransferenciaFormActivity;
import com.br.bancodigital.usuario.MinhaContaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExtratoAdapter.OnClickListener {

    private List<Notificacao> notificacaoList = new ArrayList<>();

    private List<Extrato> extratoList = new ArrayList<>();
    private List<Extrato> extratoListTemp = new ArrayList<>();

    private ExtratoAdapter extratoAdapter;
    private RecyclerView rvExtrato;

    private TextView textSaldo;
    private ProgressBar progressBar;
    private TextView textInfo;
    private TextView textNotificacao;
    private ImageView imagemPerfil;

    private Usuario usuario;
    private Notificacao notificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponente();
        configCliques();
        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaDados();
    }

    private void recuperaNotificacoes() {
        DatabaseReference notificacoesRef = FirebaseHelper.getDatabaseReference()
                .child("notificacoes")
                .child(FirebaseHelper.getIdFirebase());
        notificacoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificacaoList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    notificacao = ds.getValue(Notificacao.class);
                    notificacaoList.add(notificacao);
                }

                if (notificacaoList.isEmpty()) {
                    textNotificacao.setText("0");
                    textNotificacao.setVisibility(View.GONE);
                } else {
                    int quantidadeNotificacoes = 0;
                    for (Notificacao notificacao : notificacaoList) {
                        if (!notificacao.isLida()) {
                            quantidadeNotificacoes += 1;
                        }
                    }
                    textNotificacao.setText(String.valueOf(quantidadeNotificacoes));
                    textNotificacao.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvExtrato.setLayoutManager(new LinearLayoutManager(this));
        rvExtrato.setHasFixedSize(true);
        extratoAdapter = new ExtratoAdapter(extratoList, getBaseContext(), this);
        rvExtrato.setAdapter(extratoAdapter);
    }

    private void recuperaExtrato() {
        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference()
                .child("extratos")
                .child(FirebaseHelper.getIdFirebase());
        extratoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    extratoList.clear();
                    extratoListTemp.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Extrato extrato = ds.getValue(Extrato.class);
                        extratoListTemp.add(extrato);
                    }

                    textInfo.setText("");
                } else {
                    textInfo.setText("Você ainda não tem atividade!");
                }

                Collections.reverse(extratoListTemp);

                // mostrar as 6 ultimas movimentacoes
                for (int i = 0; i < extratoListTemp.size(); i++) {
                    if (i <= 5) {
                        extratoList.add(extratoListTemp.get(i));
                    }
                }

                progressBar.setVisibility(View.GONE);
                extratoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaDados() {
        recuperaUsuario();
        recuperaExtrato();
        recuperaNotificacoes();
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        textSaldo.setText(getString(R.string.text_valor, GetMask.getValor(usuario.getSaldo())));
        textInfo.setText("");
        progressBar.setVisibility(View.GONE);

        if (usuario.getUrlImagem() != null) {
            Picasso.get().load(usuario.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .into(imagemPerfil);
        }
    }

    private void iniciaComponente() {
        textSaldo = findViewById(R.id.textSaldo);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
        imagemPerfil = findViewById(R.id.imagemPerfil);
        rvExtrato = findViewById(R.id.rvExtrato);
        textNotificacao = findViewById(R.id.textNotificacao);
    }

    private void configCliques() {
        findViewById(R.id.cardDeposito).setOnClickListener(view -> redirecionarUsuario(DepositoFormActivity.class));

        imagemPerfil.setOnClickListener(view -> perfilUsuario());

        findViewById(R.id.cardRecarga).setOnClickListener(view ->
                startActivity(new Intent(this, RecargaFormActivity.class)));

        findViewById(R.id.cardTransferir).setOnClickListener(view -> redirecionarUsuario(TransferenciaFormActivity.class));

        findViewById(R.id.cardDeslogar).setOnClickListener(view -> {
            FirebaseHelper.getAuth().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });

        findViewById(R.id.cardExtrato).setOnClickListener(view ->
                startActivity(new Intent(this, ExtratoActivity.class)));

        findViewById(R.id.textVerTodas).setOnClickListener(view -> redirecionarUsuario(ExtratoActivity.class));

        findViewById(R.id.btnNotificacao).setOnClickListener(view -> redirecionarUsuario(NotificacoesActivity.class));

        findViewById(R.id.cardCobrar).setOnClickListener(view -> redirecionarUsuario(CobrancaFormActivity.class));

        findViewById(R.id.cardMinhaConta).setOnClickListener(view -> perfilUsuario());
    }

    private void redirecionarUsuario(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    private void perfilUsuario() {
        if (usuario != null) {
            Intent intent = new Intent(this, MinhaContaActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        } else {
            Toast.makeText(this, "\uD83E\uDD14 Recuperando seus dados!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(Extrato extrato) {

    }
}
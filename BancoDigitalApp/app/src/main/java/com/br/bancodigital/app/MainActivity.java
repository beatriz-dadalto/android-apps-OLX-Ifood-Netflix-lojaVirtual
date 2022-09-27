package com.br.bancodigital.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bancodigital.R;
import com.br.bancodigital.deposito.DepositoFormActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Usuario;
import com.br.bancodigital.recarga.RecargaFormActivity;
import com.br.bancodigital.usuario.MinhaContaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView textSaldo;
    private ProgressBar progressBar;
    private TextView textInfo;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponente();
        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaDados();
    }

    private void recuperaDados() {
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
    }

    private void iniciaComponente() {
        textSaldo = findViewById(R.id.textSaldo);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
    }

    private void configCliques() {
        findViewById(R.id.cardDeposito).setOnClickListener(view ->
                startActivity(new Intent(this, DepositoFormActivity.class)));

        findViewById(R.id.minhaConta).setOnClickListener(view -> {
            if (usuario != null) {
                Intent intent = new Intent(this, MinhaContaActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else {
                Toast.makeText(this, "\uD83E\uDD14 Ainda estamos recuperando seus dados!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.cardRecarga).setOnClickListener(view ->
                startActivity(new Intent(this, RecargaFormActivity.class)));
    }
}
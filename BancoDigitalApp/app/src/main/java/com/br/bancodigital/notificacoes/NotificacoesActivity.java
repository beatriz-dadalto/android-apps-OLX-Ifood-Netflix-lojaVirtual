package com.br.bancodigital.notificacoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.bancodigital.R;

public class NotificacoesActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        configToolbar();
        iniciaComponente();
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Notificações");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponente() {
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
    }
}
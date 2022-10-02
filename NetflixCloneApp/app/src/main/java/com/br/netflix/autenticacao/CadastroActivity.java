package com.br.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.br.netflix.R;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.btnLogin).setOnClickListener(view -> finish());
    }
}
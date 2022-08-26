package com.beatriz.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferecia";
    private EditText editNome;
    private TextView textNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.edit_nome);
        textNome = findViewById(R.id.text_nome);

        recuperarDados();
    }

    public void salvarDados(View view) {
        // recuperar o input do user
        String nome = editNome.getText().toString();

        if(!nome.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("meu_nome", nome);
            editor.apply();
        } else {
            editNome.setError("Informe seu nome");
        }
    }

    private void recuperarDados() {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        String nomeRecuperado = sharedPreferences.getString("meu_nome", "");
        textNome.setText(nomeRecuperado);

    }

}
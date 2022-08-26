package com.beatriz.dadosentreactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editIdade;
    private EditText editAltura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.edit_nome);
        editIdade = findViewById(R.id.edit_idade);
        editAltura = findViewById(R.id.edit_altura);

        findViewById(R.id.btn_enviarDados).setOnClickListener(view -> {
            // first: get input of the user
            String nome = editNome.getText().toString();
            int idade = Integer.parseInt(editIdade.getText().toString());
            double altura = Double.parseDouble(editAltura.getText().toString());

            // set object values
            Pessoa pessoa = new Pessoa(nome, idade, altura);

            // second: create a intent to navigate to another activity
            Intent intent = new Intent(this, SegundaActivity.class);

            // third: intent needs to know what info/data to take to another activity
            intent.putExtra("pessoa", pessoa);

            // fourth: go to another activity
            startActivity(intent);

        });

    }
}
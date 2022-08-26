package com.beatriz.dadosentreactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SegundaActivity extends AppCompatActivity {

    private TextView textNome;
    private TextView textIdade;
    private TextView textAltura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // enables a button to go back to previous page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // screen elements: define where data from previous activity will show up
        textNome = findViewById(R.id.text_nome);
        textIdade = findViewById(R.id.text_idade);
        textAltura = findViewById(R.id.text_altura);

        // retrieve data from the intent. data from activity_main
        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        // show on screen the user data
        textNome.setText(pessoa.getNome());
        textIdade.setText(String.valueOf(pessoa.getIdade()));
        textAltura.setText(String.valueOf(pessoa.getAltura()));
    }
}
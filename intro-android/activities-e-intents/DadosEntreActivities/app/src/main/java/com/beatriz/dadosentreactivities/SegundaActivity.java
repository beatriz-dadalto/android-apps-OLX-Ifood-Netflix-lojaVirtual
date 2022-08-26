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
        String nome = (String) getIntent().getSerializableExtra("meu_nome");
        int idade = (int) getIntent().getSerializableExtra("minha_idade");
        double altura = (double) getIntent().getSerializableExtra("minha_altura");

        // show on screen the user data
        textNome.setText(nome);
        textIdade.setText(String.valueOf(idade));
        textAltura.setText(String.valueOf(altura));
    }
}
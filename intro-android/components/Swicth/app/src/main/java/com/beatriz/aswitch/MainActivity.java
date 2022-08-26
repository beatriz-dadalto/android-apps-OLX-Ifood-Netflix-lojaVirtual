package com.beatriz.aswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwitchMaterial swNotificacoes = findViewById(R.id.swNotificacoes);
        TextView textNotificacoes = findViewById(R.id.textNotificacoes);

        swNotificacoes.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                textNotificacoes.setText("ON");
            } else {
                textNotificacoes.setText("OFF");
            }
        });
    }
}
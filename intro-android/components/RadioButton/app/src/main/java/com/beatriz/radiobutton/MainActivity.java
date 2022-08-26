package com.beatriz.radiobutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup rgOpcoes = findViewById(R.id.rgOpcoes);

        rgOpcoes.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.rbOpcao1) {
                Toast.makeText(this, "Ativou opção 1", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.rbOpcao2) {
                Toast.makeText(this, "Ativou opção 2", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.rbOpcao3) {
                Toast.makeText(this, "Ativou opção 3", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ativou opção 4", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
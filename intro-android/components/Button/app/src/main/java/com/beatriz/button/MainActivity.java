package com.beatriz.button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> Toast.makeText(this, "Botão Clicadoo", Toast.LENGTH_SHORT).show());
    }
}
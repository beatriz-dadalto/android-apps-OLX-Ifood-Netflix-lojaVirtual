package com.beatriz.togglebutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Ligado!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Desligado!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
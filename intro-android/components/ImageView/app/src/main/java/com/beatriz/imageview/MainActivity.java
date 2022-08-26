package com.beatriz.imageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        Button btnAddImagem = findViewById(R.id.btnAddImagem);

        btnAddImagem.setOnClickListener(view -> imageView.setImageResource(R.drawable.ic_launcher_foreground));

    }
}
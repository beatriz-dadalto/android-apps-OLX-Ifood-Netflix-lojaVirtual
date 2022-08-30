package com.biamailov3.ifoodclone.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;

public class UsuarioFavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_favoritos);

        inciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void inciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Favoritos");
    }
}
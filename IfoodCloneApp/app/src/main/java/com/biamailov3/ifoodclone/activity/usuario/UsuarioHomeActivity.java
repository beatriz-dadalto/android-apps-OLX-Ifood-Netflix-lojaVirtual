package com.biamailov3.ifoodclone.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;

public class UsuarioHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);
        Toast.makeText(this, "Estou aqui na home usuario", Toast.LENGTH_SHORT).show();

    }
}
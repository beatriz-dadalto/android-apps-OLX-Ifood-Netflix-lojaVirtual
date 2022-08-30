package com.biamailov3.ifoodclone.activity.empresa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;

public class EmpresaHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_home);

        Toast.makeText(this, "Estou aqui na home empresa", Toast.LENGTH_SHORT).show();
    }
}
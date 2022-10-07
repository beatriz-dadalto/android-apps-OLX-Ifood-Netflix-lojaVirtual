package com.br.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.br.ecommerce.autenticacao.LoginActivity;
import com.br.ecommerce.databinding.ActivityMainBinding;
import com.br.ecommerce.helper.FirebaseHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(view -> {
            if (FirebaseHelper.getAutenticado()) {
                FirebaseHelper.getAuth().signOut();
                Toast.makeText(this, "Usuário já autenticado", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }
}
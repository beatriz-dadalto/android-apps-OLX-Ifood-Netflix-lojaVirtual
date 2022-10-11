package com.br.ecommerce.activity.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.br.ecommerce.activity.loja.MainActivityEmpresa;
import com.br.ecommerce.activity.usuario.MainActivityUsuario;
import com.br.ecommerce.R;
import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(getMainLooper()).postDelayed(this::verificaAcesso, 2000);
    }

    private void verificaAcesso() {
        if (FirebaseHelper.getAutenticado()) {
            recuperaAcesso();
        } else {
            finish();
            startActivity(new Intent(this, MainActivityUsuario.class));
        }
    }

    private void recuperaAcesso() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // usuario
                    startActivity(new Intent(getBaseContext(), MainActivityUsuario.class));
                } else { // loja
                    startActivity(new Intent(getBaseContext(), MainActivityEmpresa.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
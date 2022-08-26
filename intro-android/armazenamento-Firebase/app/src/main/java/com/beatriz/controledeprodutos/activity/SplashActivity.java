package com.beatriz.controledeprodutos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.beatriz.controledeprodutos.R;
import com.beatriz.controledeprodutos.autenticacao.LoginActivity;
import com.beatriz.controledeprodutos.helper.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::verificarLogin, 3000);

    }

    private void verificarLogin() {
        if (FirebaseHelper.isAutenticado()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity( new Intent(this, LoginActivity.class));
        }
    }
}
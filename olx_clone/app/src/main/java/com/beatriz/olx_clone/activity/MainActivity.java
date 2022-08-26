package com.beatriz.olx_clone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.beatriz.olx_clone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();


    }

    private void iniciarComponentes() {
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        int id = getIntent().getIntExtra("id", 0);
        if (id == 2) {
            // se for 2 vai iniciar na opcao 2 da bottomshit
            navView.setSelectedItemId(R.id.menu_meus_anuncios);
        }
    }
}
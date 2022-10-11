package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityMainEmpresaBinding;
import com.br.ecommerce.databinding.ActivityMainUsuarioBinding;

public class MainActivityEmpresa extends AppCompatActivity {

    private ActivityMainEmpresaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainEmpresaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }
}
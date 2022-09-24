package com.example.ifoodclone.activity.usuario;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ifoodclone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsuarioHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        int id = getIntent().getIntExtra("id", 0);
        if(id == 3){
            bottomNavigationView.setSelectedItemId(R.id.menu_pedidos);
        }

    }

}
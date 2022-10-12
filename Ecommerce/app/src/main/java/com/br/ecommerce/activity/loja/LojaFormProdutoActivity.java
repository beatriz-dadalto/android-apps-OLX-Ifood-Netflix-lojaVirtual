package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityLojaFormProdutoBinding;
import com.br.ecommerce.databinding.BottomSheetFormProdutoBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class LojaFormProdutoActivity extends AppCompatActivity {

    private ActivityLojaFormProdutoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaFormProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.imagemProduto0.setOnClickListener(view -> showBottomSheet());
        binding.imagemProduto1.setOnClickListener(view -> showBottomSheet());
        binding.imagemProduto2.setOnClickListener(view -> showBottomSheet());
    }

    private void showBottomSheet() {
        BottomSheetFormProdutoBinding bottomSheetBinding =
                BottomSheetFormProdutoBinding.inflate(LayoutInflater.from(this));

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(this, R.style.BottomSheetTheme);

        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();

        bottomSheetBinding.btnCamera.setOnClickListener(view -> {
            Toast.makeText(this, "Câmera", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetBinding.btnGaleria.setOnClickListener(view -> {
            Toast.makeText(this, "Galeria", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetBinding.btnCancelar.setOnClickListener(view -> {
            Toast.makeText(this, "Cancelar", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });
    }
}
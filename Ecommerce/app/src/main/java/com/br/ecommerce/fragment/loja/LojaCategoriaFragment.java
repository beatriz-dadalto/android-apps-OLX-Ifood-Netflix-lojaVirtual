package com.br.ecommerce.fragment.loja;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.DialogFormCategoriaBinding;
import com.br.ecommerce.databinding.FragmentLojaCategoriaBinding;

public class LojaCategoriaFragment extends Fragment {

    private FragmentLojaCategoriaBinding binding;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLojaCategoriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configCliques();
    }

    private void configCliques() {
        binding.btnAddCategoria.setOnClickListener(view -> showDialog());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                R.style.CustomAlertDialog);

        DialogFormCategoriaBinding categoriaBinding = DialogFormCategoriaBinding.
                inflate(LayoutInflater.from(getContext()));

        categoriaBinding.btnFechar.setOnClickListener(view -> dialog.dismiss());
        categoriaBinding.btnSalvar.setOnClickListener(view -> dialog.dismiss());

        builder.setView(categoriaBinding.getRoot());
        dialog = builder.create();
        dialog.show();
    }
}
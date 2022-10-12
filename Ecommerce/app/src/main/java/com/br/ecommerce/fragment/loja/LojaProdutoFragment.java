package com.br.ecommerce.fragment.loja;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.activity.loja.LojaFormProdutoActivity;
import com.br.ecommerce.databinding.FragmentLojaProdutoBinding;


public class LojaProdutoFragment extends Fragment {

    private FragmentLojaProdutoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLojaProdutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        confiCliques();
    }

    private void confiCliques() {
        binding.toolbar.btnAdd.setOnClickListener(view ->
                startActivity(new Intent(requireContext(), LojaFormProdutoActivity.class)));
    }
}
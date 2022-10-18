package com.br.ecommerce.fragment.loja;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.R;
import com.br.ecommerce.activity.loja.LojaFormProdutoActivity;
import com.br.ecommerce.adapter.LojaProdutoAdapter;
import com.br.ecommerce.databinding.DialogLojaProdutoBinding;
import com.br.ecommerce.databinding.FragmentLojaProdutoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LojaProdutoFragment extends Fragment implements LojaProdutoAdapter.OnClickLister {

    private FragmentLojaProdutoBinding binding;
    private DialogLojaProdutoBinding dialogBinding;

    private List<Produto> produtoList = new ArrayList<>();
    private LojaProdutoAdapter lojaProdutoAdapter;

    private AlertDialog dialog;

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
        configRv();
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaProdutos();
    }

    private void confiCliques() {
        binding.toolbar.btnAdd.setOnClickListener(view ->
                startActivity(new Intent(requireContext(), LojaFormProdutoActivity.class)));
    }

    private void configRv() {
        binding.rvProdutos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProdutos.setHasFixedSize(true);
        lojaProdutoAdapter = new LojaProdutoAdapter(produtoList, requireContext(), this);
        binding.rvProdutos.setAdapter(lojaProdutoAdapter);
    }

    private void recuperaProdutos() {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos");
        produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    produtoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Produto produto = ds.getValue(Produto.class);
                        produtoList.add(produto);
                    }
                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhum produto cadastrado.");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(produtoList);
                lojaProdutoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(Produto produto) {
        showDialog(produto);
    }

    private void showDialog(Produto produto) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);

        dialogBinding = DialogLojaProdutoBinding.inflate(LayoutInflater.from(requireContext()));

        for (int i = 0; i < produto.getUrlsImagens().size(); i++) {
            if (produto.getUrlsImagens().get(i).getIndex() == 0) {
                Picasso.get().load(produto.getUrlsImagens().get(i).getCaminhoImagem())
                        .into(dialogBinding.imagemProduto);
            }
        }
        dialogBinding.txtNomeProduto.setText(produto.getTitulo());
        dialogBinding.btnFechar.setOnClickListener(view -> dialog.dismiss());

        builder.setView(dialogBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }
}
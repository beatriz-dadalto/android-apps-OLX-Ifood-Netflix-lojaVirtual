package com.br.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.LojaProdutoAdapter;
import com.br.ecommerce.databinding.FragmentUsuarioFavoritoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Favorito;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioFavoritoFragment extends Fragment implements LojaProdutoAdapter.OnClickLister, LojaProdutoAdapter.OnClickFavorito {

    private FragmentUsuarioFavoritoBinding binding;

    private DatabaseReference favoritoRef;
    private ValueEventListener eventListener;

    private final List<Produto> produtoList = new ArrayList<>();
    private final List<String> idsFavoritos = new ArrayList<>();

    private LojaProdutoAdapter lojaProdutoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioFavoritoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configRvProdutos();
        recuperaFavoritos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        favoritoRef.removeEventListener(eventListener);
        binding = null;
    }

    private void configRvProdutos() {
        binding.rvProdutos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProdutos.setHasFixedSize(true);
        lojaProdutoAdapter = new LojaProdutoAdapter(R.layout.item_produto_adapter, produtoList, requireContext(), true, idsFavoritos, this, this);
        binding.rvProdutos.setAdapter(lojaProdutoAdapter);
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            favoritoRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            eventListener = favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }

                    Collections.reverse(idsFavoritos);
                    listEmpty();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void recuperaProdutos() {
        produtoList.clear();
        for (int i = 0; i < idsFavoritos.size(); i++) {
            DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                    .child("produtos")
                    .child(idsFavoritos.get(i));
            produtoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Produto produto = snapshot.getValue(Produto.class);
                    produtoList.add(produto);

                    if (produtoList.size() == idsFavoritos.size()) {
                        binding.progressBar.setVisibility(View.GONE);
                        lojaProdutoAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void listEmpty() {
        if (idsFavoritos.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.textInfo.setText("Nenhum produto na sua lista de favoritos.");
        } else {
            binding.textInfo.setText("");
            recuperaProdutos();
        }
    }

    @Override
    public void onClick(Produto produto) {

    }

    @Override
    public void onClickFavorito(Produto produto) {
        if (!idsFavoritos.contains(produto.getId())) {
            idsFavoritos.add(produto.getId());
            produtoList.add(produto);
        } else {
            idsFavoritos.remove(produto.getId());
            produtoList.remove(produto);
        }
        Favorito.salvar(idsFavoritos);

        lojaProdutoAdapter.notifyDataSetChanged();
    }
}
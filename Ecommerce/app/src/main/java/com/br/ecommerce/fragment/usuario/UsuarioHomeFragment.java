package com.br.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.CategoriaAdapter;
import com.br.ecommerce.adapter.LojaProdutoAdapter;
import com.br.ecommerce.databinding.FragmentUsuarioHomeBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Categoria;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioHomeFragment extends Fragment implements CategoriaAdapter.OnClick, LojaProdutoAdapter.OnClickLister {

    private FragmentUsuarioHomeBinding binding;

    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();
    private LojaProdutoAdapter lojaProdutoAdapter;
    private List<Produto> produtoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configRvCategorias();
        configRvProdutos();
        recuperaCategorias();
        recuperaProdutos();
    }

    private void configRvCategorias() {
        binding.rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(R.layout.item_categoria_horizontal,
                true, categoriaList, this);
        binding.rvCategorias.setAdapter(categoriaAdapter);
    }

    private void recuperaCategorias() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias");
        categoriaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                categoriaList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Categoria categoria = ds.getValue(Categoria.class);
                    categoriaList.add(categoria);
                }

                Collections.reverse(categoriaList);
                categoriaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRvProdutos() {
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
                }

                listEmpty();

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(produtoList);
                lojaProdutoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listEmpty() {
        if (produtoList.isEmpty()) {
            binding.textInfo.setText("Nenhum produto cadastrado");
        } else {
            binding.textInfo.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickListener(Categoria categoria) {
        Toast.makeText(requireContext(), categoria.getNome(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Produto produto) {

    }
}
package com.br.ecommerce.fragment.usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.activity.usuario.DetalhesProdutoActivity;
import com.br.ecommerce.adapter.CategoriaAdapter;
import com.br.ecommerce.adapter.LojaProdutoAdapter;
import com.br.ecommerce.databinding.FragmentUsuarioHomeBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Categoria;
import com.br.ecommerce.model.Favorito;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UsuarioHomeFragment extends Fragment implements CategoriaAdapter.OnClick, LojaProdutoAdapter.OnClickLister, LojaProdutoAdapter.OnClickFavorito {

    private FragmentUsuarioHomeBinding binding;

    private final List<Categoria> categoriaList = new ArrayList<>();
    private final List<Produto> produtoList = new ArrayList<>();
    private final List<Produto> filtroProdutoCategoriaList = new ArrayList<>();
    private final List<String> idsFavoritos = new ArrayList<>();

    private CategoriaAdapter categoriaAdapter;
    private LojaProdutoAdapter lojaProdutoAdapter;

    private Categoria categoriaSelecionada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsuarioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configRvCategorias();

        configSearchView();

        recuperaDados();
    }

    private void configSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisa) {
                ocultaTeclado();

                filtraProdutoNome(pesquisa);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(v -> {
            EditText edtSerachView = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            edtSerachView.getText().clear();
            edtSerachView.clearFocus();
            ocultaTeclado();
            filtraProdutoCategoria();
        });

    }

    private void recuperaDados() {
        recuperaCategorias();

        recuperaProdutos();

        recuperaFavoritos();
    }

    private void configRvCategorias() {
        binding.rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(R.layout.item_categoria_horizontal, true, categoriaList, this);
        binding.rvCategorias.setAdapter(categoriaAdapter);
    }

    private void configRvProdutos(List<Produto> produtoList) {
        binding.rvProdutos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProdutos.setHasFixedSize(true);
        lojaProdutoAdapter = new LojaProdutoAdapter(R.layout.item_produto_adapter, produtoList, requireContext(), true, idsFavoritos, this, this);
        binding.rvProdutos.setAdapter(lojaProdutoAdapter);
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

    private void recuperaProdutos() {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos");
        produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                produtoList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Produto produto = ds.getValue(Produto.class);
                    produtoList.add(produto);
                }

                listEmpty(produtoList);

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(produtoList);

                configRvProdutos(produtoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void filtraProdutoCategoria() {
        if (!categoriaSelecionada.isTodas()) {
            for (Produto produto : produtoList) {
                if (produto.getIdsCategorias().contains(categoriaSelecionada.getId())) {
                    if(!filtroProdutoCategoriaList.contains(produto)){
                        filtroProdutoCategoriaList.add(produto);
                    }
                }
            }

            configRvProdutos(filtroProdutoCategoriaList);
        } else {
            filtroProdutoCategoriaList.clear();
            configRvProdutos(produtoList);
        }
    }

    private void filtraProdutoNome(String pesquisa) {
        List<Produto> filtroProdutoNomeList = new ArrayList<>();

        if (!filtroProdutoCategoriaList.isEmpty()) {
            for (Produto produto : filtroProdutoCategoriaList) {
                if (produto.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))) {
                    filtroProdutoNomeList.add(produto);
                }
            }
        } else {
            for (Produto produto : produtoList) {
                if (produto.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))) {
                    filtroProdutoNomeList.add(produto);
                }
            }
        }
        configRvProdutos(filtroProdutoNomeList);
    }

    private void listEmpty(List<Produto> produtoList) {
        if (produtoList.isEmpty()) {
            binding.textInfo.setText("Nenhum produto localizado.");
        } else {
            binding.textInfo.setText("");
        }
    }

    // Oculta o teclado do dispotivo
    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.searchView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClickListener(Categoria categoria) {
        this.categoriaSelecionada = categoria;
        filtraProdutoCategoria();
    }

    @Override
    public void onClick(Produto produto) {
        Intent intent = new Intent(requireContext(), DetalhesProdutoActivity.class);
        intent.putExtra("produtoSelecionado", produto);
        startActivity(intent);
    }

    @Override
    public void onClickFavorito(Produto produto) {
        if (!idsFavoritos.contains(produto.getId())) {
            idsFavoritos.add(produto.getId());
        } else {
            idsFavoritos.remove(produto.getId());
        }
        Favorito.salvar(idsFavoritos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
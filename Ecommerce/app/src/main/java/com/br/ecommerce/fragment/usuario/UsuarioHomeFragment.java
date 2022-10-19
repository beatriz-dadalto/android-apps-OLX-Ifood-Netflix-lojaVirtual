package com.br.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.CategoriaAdapter;
import com.br.ecommerce.databinding.FragmentUsuarioHomeBinding;
import com.br.ecommerce.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class UsuarioHomeFragment extends Fragment implements CategoriaAdapter.OnClick {

    private FragmentUsuarioHomeBinding binding;

    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();

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

        configRv();
    }

    private void configRv() {
        binding.rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(R.layout.item_categoria_horizontal,
                true, categoriaList, this);
        binding.rvCategorias.setAdapter(categoriaAdapter);
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
}
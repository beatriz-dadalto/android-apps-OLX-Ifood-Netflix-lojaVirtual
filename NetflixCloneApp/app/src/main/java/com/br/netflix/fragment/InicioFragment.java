package com.br.netflix.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.netflix.R;
import com.br.netflix.adapter.AdapterCategoria;
import com.br.netflix.autenticacao.LoginActivity;
import com.br.netflix.helper.FirebaseHelper;
import com.br.netflix.model.Categoria;
import com.br.netflix.model.Post;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private AdapterCategoria adapterCategoria;

    private List<Categoria> categoriaList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();

    private RecyclerView rvCategorias;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnLogin).setOnClickListener(view1 -> {
            if (FirebaseHelper.getAutenticado()) {
                Toast.makeText(requireContext(), "Usuário já autenticado!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            }
        });

        iniciaComponentes(view);
        configRv();
    }

    private void configRv() {
        rvCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategorias.setHasFixedSize(true);
        adapterCategoria = new AdapterCategoria(categoriaList, getContext());
        rvCategorias.setAdapter(adapterCategoria);
    }

    private void iniciaComponentes(View view) {
        rvCategorias = view.findViewById(R.id.rvCategorias);
        progressBar = view.findViewById(R.id.progressBar);
    }
}
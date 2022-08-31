package com.biamailov3.ifoodclone.fragment.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaFormProdutoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmpresaProdutoFragment extends Fragment {

    private FloatingActionButton fabAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_produto, container, false);

        iniciarComponentes(view);
        configCliques();

        return view;
    }

    private void configCliques() {
        fabAdd.setOnClickListener(view -> startActivity(new Intent(requireActivity(), EmpresaFormProdutoActivity.class)));
    }

    private void iniciarComponentes(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
    }
}
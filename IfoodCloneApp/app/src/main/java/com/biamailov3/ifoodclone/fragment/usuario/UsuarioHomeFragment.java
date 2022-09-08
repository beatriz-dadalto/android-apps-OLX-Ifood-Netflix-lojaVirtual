package com.biamailov3.ifoodclone.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.EmpresaAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioHomeFragment extends Fragment implements EmpresaAdapter.OnClickListener {

    private EmpresaAdapter empresaAdapter;
    private List<Empresa> empresaList = new ArrayList<>();

    private RecyclerView rvEmpresas;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_home, container, false);

        iniciarComponentes(view);
        configRv();
        recuperarEmpresas();

        return view;
    }

    private void configRv() {
        rvEmpresas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEmpresas.setHasFixedSize(true);
        empresaAdapter = new EmpresaAdapter(empresaList, this, requireContext());
        rvEmpresas.setAdapter(empresaAdapter);
    }

    private void recuperarEmpresas() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas");
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    empresaList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Empresa empresa = ds.getValue(Empresa.class);
                        empresaList.add(empresa);
                    }
                } else {
                    textInfo.setText("Nenhuma empresa cadastrada");
                }

                progressBar.setVisibility(View.GONE);
                textInfo.setText("");
                Collections.reverse(empresaList);
                empresaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciarComponentes(View view) {
        rvEmpresas = view.findViewById(R.id.rv_empresas);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
    }

    @Override
    public void onClick(Empresa empresa) {
        // TODO ir para a activity cardapio
        Toast.makeText(requireContext(), empresa.getNome(), Toast.LENGTH_SHORT).show();
    }
}
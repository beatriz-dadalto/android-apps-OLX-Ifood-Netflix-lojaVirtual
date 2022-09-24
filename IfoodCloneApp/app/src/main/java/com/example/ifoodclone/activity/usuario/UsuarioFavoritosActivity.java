package com.example.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.empresa.EmpresaCardapioActivity;
import com.example.ifoodclone.adapter.EmpresasAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioFavoritosActivity extends AppCompatActivity implements EmpresasAdapter.OnClickListener {

    private EmpresasAdapter empresasAdapter;
    private List<Empresa> empresaList = new ArrayList<>();
    private final List<String> favoritoList = new ArrayList<>();

    private RecyclerView rv_empresas;
    private ProgressBar progressBar;
    private TextView text_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_favoritos);

        iniciaComponentes();

        configRv();

        configCliques();

    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaFavorito();
    }

    private void configRv(){
        rv_empresas.setLayoutManager(new LinearLayoutManager(this));
        rv_empresas.setHasFixedSize(true);
        empresasAdapter = new EmpresasAdapter(empresaList, this, this);
        rv_empresas.setAdapter(empresasAdapter);
    }

    private void recuperaEmpresas(){
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas");
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresaList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Empresa empresa = ds.getValue(Empresa.class);
                    if(favoritoList.contains(empresa.getId())){
                        empresaList.add(empresa);
                    }
                }

                text_info.setText("");
                progressBar.setVisibility(View.GONE);
                Collections.reverse(empresaList);
                empresasAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaFavorito(){
        if(FirebaseHelper.getAutenticado()){
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        favoritoList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String idFavorito = ds.getValue(String.class);
                            favoritoList.add(idFavorito);
                        }

                        recuperaEmpresas();

                    }else {
                        empresaList.clear();
                        empresasAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        text_info.setText("Nenhum estabelecimento adicionado.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Favoritos");

        rv_empresas = findViewById(R.id.rv_empresas);
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
    }

    @Override
    public void OnClick(Empresa empresa) {
        Intent intent = new Intent(this, EmpresaCardapioActivity.class);
        intent.putExtra("empresaSelecionada", empresa);
        startActivity(intent);
    }
}
package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.AddMaisAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.AddMais;
import com.biamailov3.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaAddMaisActivity extends AppCompatActivity implements AddMaisAdapter.OnClickListener {

    private List<Produto> produtoList = new ArrayList<>();
    private List<String> addMaisList = new ArrayList<>();

    private AddMaisAdapter addMaisAdapter;

    private RecyclerView rvProdutos;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_add_mais);

        iniciarComponentes();
        recuperarProdutos();
        configRv();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void configRv() {
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        addMaisAdapter = new AddMaisAdapter(produtoList, addMaisList, this, this);
        rvProdutos.setAdapter(addMaisAdapter);
    }

    private void recuperarProdutos() {
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    produtoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Produto produto = ds.getValue(Produto.class);
                        produtoList.add(produto);
                    }

                    recuperarItens();
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(produtoList);
                addMaisAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarItens() {
        DatabaseReference addMaisRef = FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(FirebaseHelper.getIdFirebase());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idProduto = ds.getValue(String.class);
                        addMaisList.add(idProduto);
                    }

                    configProdutos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configProdutos() {
        for (Produto produto : produtoList) {
            if (addMaisList.contains(produto.getId())) {
                produto.setAddMais(true);
            }
        }
        addMaisAdapter.notifyDataSetChanged();
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Compre Junto");

        rvProdutos = findViewById(R.id.rv_produtos);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(String idProduto, Boolean status) {
        if (status) {
            if (!addMaisList.contains(idProduto)) addMaisList.add(idProduto);
        } else {
            addMaisList.remove(idProduto);
        }

        AddMais.salvar(addMaisList);
    }
}
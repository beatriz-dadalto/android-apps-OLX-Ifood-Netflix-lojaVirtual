package com.example.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.AddMaisAdapter;
import com.example.ifoodclone.model.AddMais;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Produto;
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

    private RecyclerView rv_produtos;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_add_mais);

        iniciaComponentes();

        recuperaProdutos();

        configCliques();

        configRv();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configRv(){
        rv_produtos.setLayoutManager(new LinearLayoutManager(this));
        rv_produtos.setHasFixedSize(true);
        addMaisAdapter = new AddMaisAdapter(produtoList, addMaisList, this, this);
        rv_produtos.setAdapter(addMaisAdapter);
    }

    private void recuperaProdutos(){
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    produtoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Produto produto = ds.getValue(Produto.class);
                        produtoList.add(produto);
                    }

                    recuperaItens();

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

    private void recuperaItens(){
        DatabaseReference addMaisRef = FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(FirebaseHelper.getIdFirebase());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
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

    private void configProdutos(){
        for (Produto produto : produtoList){
            if(addMaisList.contains(produto.getId())){
                produto.setAddMais(true);
            }
        }
        addMaisAdapter.notifyDataSetChanged();
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Compre junto");

        rv_produtos = findViewById(R.id.rv_produtos);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void OnClick(String idProduto, Boolean status) {
        if(status){
            if(!addMaisList.contains(idProduto)) addMaisList.add(idProduto);
        }else {
            addMaisList.remove(idProduto);
        }
        AddMais.salvar(addMaisList);
    }

}
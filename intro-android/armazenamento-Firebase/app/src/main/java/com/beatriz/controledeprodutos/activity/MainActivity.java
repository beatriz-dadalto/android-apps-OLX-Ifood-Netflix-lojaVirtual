package com.beatriz.controledeprodutos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.controledeprodutos.adapter.AdapterProduto;
import com.beatriz.controledeprodutos.autenticacao.LoginActivity;
import com.beatriz.controledeprodutos.helper.FirebaseHelper;
import com.beatriz.controledeprodutos.model.Produto;
import com.beatriz.controledeprodutos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private AdapterProduto adapterProduto;
    private SwipeableRecyclerView rvProdutos;
    private List<Produto> produtoList = new ArrayList<>();

    private ImageButton ibAdd;
    private ImageButton ibVerMais;
    private TextView textInfo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        ouvinteCliques();

        configRecyclerView();
    }

    private void iniciarComponentes() {
        ibAdd = findViewById(R.id.ib_add);
        ibVerMais = findViewById(R.id.ib_ver_mais);
        textInfo = findViewById(R.id.text_info);
        rvProdutos = findViewById(R.id.rvProdutos);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarProdutos();
    }

    private void recuperarProdutos() {
        // sempre buscar a referencia do banco quando for salvar ou recuperar
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase()); // id user logado

            produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtoList.clear();
                // em snapshot tem todas as informações do banco
                for (DataSnapshot snap : snapshot.getChildren()) {
                    // recuperando todos produtos do user logado
                    Produto produto = snap.getValue(Produto.class);

                    Log.i("INFOTESTE", "onDataChange: " + produto.getUrlImagem());

                    produtoList.add(produto);
                }
                isProductListEmpty();

                // mostrar o ultimo added primeiro
                Collections.reverse(produtoList);
                //exibir em tela
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ouvinteCliques() {
        ibAdd.setOnClickListener(view -> {
            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FormProdutoActivity.class));
        });

        ibVerMais.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, ibVerMais);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_sobre) {
                    Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.menu_sair) {
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            });

            // show on screen
            popupMenu.show();
        });
    }

    // lib swipe na RecyclerView
    private void configRecyclerView() {

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtoList, this);
        rvProdutos.setAdapter(adapterProduto);

        rvProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                Produto produto = produtoList.get(position);


                produtoList.remove(produto);
                produto.deletarProduto();
                adapterProduto.notifyItemRemoved(position);

                isProductListEmpty();
            }
        });
    }

    private void isProductListEmpty() {
        if (produtoList.size() == 0) {
            textInfo.setText("Nenhum produto cadastrado");
            textInfo.setVisibility(View.VISIBLE);
        } else {
            textInfo.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickListener(Produto produto) {
        Toast.makeText(this, produto.getNome(), Toast.LENGTH_SHORT).show();

        // quando clicar levar para a tela de form com campos para alterar os dados
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }
}
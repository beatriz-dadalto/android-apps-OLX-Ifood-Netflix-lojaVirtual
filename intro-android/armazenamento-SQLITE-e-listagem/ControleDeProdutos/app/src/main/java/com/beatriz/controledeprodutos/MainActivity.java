package com.beatriz.controledeprodutos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private AdapterProduto adapterProduto;
    private SwipeableRecyclerView rvProdutos;
    private List<Produto> produtoList = new ArrayList<>();

    private ImageButton ibAdd;
    private ImageButton ibVerMais;
    private TextView textInfo;

    private ProdutoDAO produtoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        produtoDAO = new ProdutoDAO(this);
        produtoList = produtoDAO.getListaProdutos();

        ibAdd = findViewById(R.id.ib_add);
        ibVerMais = findViewById(R.id.ib_ver_mais);
        textInfo = findViewById(R.id.text_info);
        rvProdutos = findViewById(R.id.rvProdutos);

        // carregar em memoria
        //carregarLista();

        configRecyclerView();

        ouvinteCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        configRecyclerView();
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
                }
                return true;
            });

            // show on screen
            popupMenu.show();
        });
    }

    private void configRecyclerView() {

        produtoList.clear();
        produtoList = produtoDAO.getListaProdutos();

        isProductListEmpty();

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

                produtoDAO.deletarProduto(produto);
                produtoList.remove(produto);
                adapterProduto.notifyItemRemoved(position);

                isProductListEmpty();
            }
        });
    }

    private void isProductListEmpty() {
        if (produtoList.size() == 0) {
            textInfo.setVisibility(View.VISIBLE);
        } else {
            textInfo.setVisibility(View.GONE);
        }
    }

    /*
    private void carregarLista() {

        Produto produto1 = new Produto();

        produto1.setNome("Monitor 34 LG");
        produto1.setEstoque(45);
        produto1.setValor(1349.99);

        produtoList.add(produto1);

    }*/

    @Override
    public void onClickListener(Produto produto) {
        Toast.makeText(this, produto.getNome(), Toast.LENGTH_SHORT).show();

        // quando clicar levar para a tela de form com campos para alterar os dados
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }
}
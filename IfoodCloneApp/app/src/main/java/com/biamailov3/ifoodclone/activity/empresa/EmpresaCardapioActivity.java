package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.autenticacao.LoginActivity;
import com.biamailov3.ifoodclone.adapter.CardapioAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Categoria;
import com.biamailov3.ifoodclone.model.CategoriaCardapio;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Favorito;
import com.biamailov3.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaCardapioActivity extends AppCompatActivity {

    private CardapioAdapter cardapioAdapter;

    private  List<Produto> produtoList = new ArrayList<>();
    private List<Categoria> categoriaList = new ArrayList<>();
    private List<String> idsCategoriaList = new ArrayList<>();
    private List<CategoriaCardapio> categoriaCardapioList = new ArrayList<>();

    private List<String> favoritosList = new ArrayList<>();
    private Favorito favorito = new Favorito();

    private ImageView imgLogoEmpresa;
    private TextView textEmpresa;
    private TextView textCategoriaEmpresa;
    private TextView textTempoMinimo;
    private TextView textTempoMaximo;
    private TextView textTaxaEntrega;
    private LikeButton btnLike;

    private RecyclerView rvCategorias;
    private ProgressBar progressBar;
    private TextView textInfo;

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_cardapio);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresa = (Empresa) bundle.getSerializable("empresaSelecionada");

            configDados();
        }

        configRv();
        configCliques();
        recuperarFavorito();
        recuperarProdutos();
    }

    private void configRv() {
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        rvCategorias.setHasFixedSize(true);
        cardapioAdapter = new CardapioAdapter(categoriaCardapioList, getBaseContext());
        rvCategorias.setAdapter(cardapioAdapter);
    }

    private void recuperarProdutos(){
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(empresa.getId());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    produtoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Produto produto = ds.getValue(Produto.class);
                        produtoList.add(produto);
                        configListCategoria(produto);
                    }

                    if (!idsCategoriaList.isEmpty()) {
                        recuperarCategorias();
                    }

                    textInfo.setText("");
                }else {
                    progressBar.setVisibility(View.GONE);
                    textInfo.setText("Nenhum produto cadastrado.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configListCategoria(Produto produto) {
        boolean contemId = false;
        for (String idCategoria : idsCategoriaList) {
            if (idCategoria.equals(produto.getIdCategoria())) {
                contemId = true;
                break;
            }
        }

        if (!contemId) idsCategoriaList.add(produto.getIdCategoria());
    }

    private void recuperarCategorias() {
        for (String idCategoria : idsCategoriaList) {
            DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                    .child("categorias")
                    .child(empresa.getId())
                    .child(idCategoria);
            categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Categoria categoria = snapshot.getValue(Categoria.class);
                    categoriaList.add(categoria);

                    if (categoriaList.size() == idsCategoriaList.size()) {
                        produtoPorCategoria();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // cardapio
    private void produtoPorCategoria() {
        List<Produto> produtoListTemp = new ArrayList<>();

        for (Categoria categoria : categoriaList) {
            for (Produto produto : produtoList) {
                if (categoria.getId().equals(produto.getIdCategoria())) {
                    produtoListTemp.add(produto);
                }
            }

            categoriaCardapioList.add(new CategoriaCardapio(categoria.getNome(), produtoListTemp));
            cardapioAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            produtoListTemp = new ArrayList<>();
        }
    }

    private void configDados() {
        Picasso.get().load(empresa.getUrlLogo()).into(imgLogoEmpresa);
        textEmpresa.setText(empresa.getNome());
        textCategoriaEmpresa.setText(empresa.getCategoria());
        textTempoMinimo.setText(empresa.getTempoMinEntrega() + "-");
        textTempoMaximo.setText(empresa.getTempoMaxEntrega() + " min");

        if (empresa.getTaxaEntrega() > 0) {
            textTaxaEntrega.setText(getString(R.string.text_valor, GetMask.getValor(empresa.getTaxaEntrega())));
        } else {
            textTaxaEntrega.setTextColor(Color.parseColor("#2ED67E"));
            textTaxaEntrega.setText("Entrega Grátis");
        }
    }

    private void recuperarFavorito() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        favoritosList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String idFavorito = ds.getValue(String.class);
                            favoritosList.add(idFavorito);
                        }

                        if (favoritosList.contains(empresa.getId())) {
                            btnLike.setLiked(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        btnLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                configFavorito();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                configFavorito();
            }
        });
    }

    private void configFavorito() {
        if (FirebaseHelper.getAutenticado()) {
            if (!favoritosList.contains(empresa.getId())) {
                favoritosList.add(empresa.getId());
            } else {
                favoritosList.remove(empresa.getId());
            }

            favorito.setFavoritoList(favoritosList);
            favorito.salvar();
        } else {
            btnLike.setLiked(false);
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Cardápio");

        imgLogoEmpresa = findViewById(R.id.img_logo_empresa);
        textEmpresa = findViewById(R.id.text_empresa);
        textCategoriaEmpresa = findViewById(R.id.text_categoria_empresa);
        textTempoMinimo = findViewById(R.id.text_tempo_minimo);
        textTempoMaximo = findViewById(R.id.text_tempo_maximo);
        textTaxaEntrega = findViewById(R.id.text_taxa_entrega);
        btnLike = findViewById(R.id.btn_like);
        rvCategorias = findViewById(R.id.rv_categorias);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
    }
}
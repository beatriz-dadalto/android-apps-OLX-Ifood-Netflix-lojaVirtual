package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.autenticacao.LoginActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Favorito;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EmpresaCardapioActivity extends AppCompatActivity {

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

        configCliques();
        recuperarFavorito();
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
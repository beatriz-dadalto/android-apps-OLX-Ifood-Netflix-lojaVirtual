package com.br.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.SliderAdapter;
import com.br.ecommerce.databinding.ActivityDetalhesProdutoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Favorito;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

public class DetalhesProdutoActivity extends AppCompatActivity {

    private ActivityDetalhesProdutoBinding binding;

    private final List<String> idsFavoritos = new ArrayList<>();

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
        getExtra();
        recuperaFavoritos();
    }

    private void configCliques() {
        binding.include.textTitulo.setText("Detalhes do Produto");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());

        binding.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (FirebaseHelper.getAutenticado()) {
                    idsFavoritos.add(produto.getId());
                    Favorito.salvar(idsFavoritos);
                } else {
                    Toast.makeText(DetalhesProdutoActivity.this, "Entre na sua conta para poder favoritar", Toast.LENGTH_SHORT).show();
                    binding.likeButton.setLiked(false);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                idsFavoritos.remove(produto.getId());
                Favorito.salvar(idsFavoritos);
            }
        });
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }

                    binding.likeButton.setLiked(idsFavoritos.contains(produto.getId()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getExtra() {
        produto = (Produto) getIntent().getSerializableExtra("produtoSelecionado");

        configDados();
    }

    private void configDados() {
        binding.sliderView.setSliderAdapter(new SliderAdapter(produto.getUrlsImagens()));
        binding.sliderView.startAutoCycle();
        binding.sliderView.setScrollTimeInSec(3);
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);

        binding.textProduto.setText(produto.getTitulo());
        binding.textDescricao.setText(produto.getDescricao());
        binding.textValor.setText(getString(R.string.valor, GetMask.getValor(produto.getValorAtual())));
    }
}
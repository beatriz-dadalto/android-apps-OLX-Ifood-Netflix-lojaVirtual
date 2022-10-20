package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.SliderAdapter;
import com.br.ecommerce.databinding.ActivityDetalhesProdutoBinding;
import com.br.ecommerce.model.Produto;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

public class DetalhesProdutoActivity extends AppCompatActivity {

    private ActivityDetalhesProdutoBinding binding;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getExtra();
        configDados();
    }

    private void getExtra() {
        produto = (Produto) getIntent().getSerializableExtra("produtoSelecionado");
    }

    private void configDados() {
        binding.sliderView.setSliderAdapter(new SliderAdapter(produto.getUrlsImagens()));
        binding.sliderView.startAutoCycle();
        binding.sliderView.setScrollTimeInSec(3);
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
    }
}
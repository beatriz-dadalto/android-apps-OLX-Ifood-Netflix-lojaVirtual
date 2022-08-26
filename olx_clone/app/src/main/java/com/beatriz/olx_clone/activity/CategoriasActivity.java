package com.beatriz.olx_clone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.adapter.AdapterCategoria;
import com.beatriz.olx_clone.helper.CategoriaList;
import com.beatriz.olx_clone.model.Categoria;

public class CategoriasActivity extends AppCompatActivity implements AdapterCategoria.OnCLickListener {
    
    private RecyclerView rvCategorias;
    private AdapterCategoria adapterCategoria;
    private boolean todasCategorias = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            todasCategorias = (boolean) bundle.getSerializable("todas");
        }
        configCliques();
        iniciarRv();
    }

    private void iniciarRv() {
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        rvCategorias.setHasFixedSize(true);
        adapterCategoria = new AdapterCategoria(CategoriaList.getList(todasCategorias), this);
        rvCategorias.setAdapter(adapterCategoria);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Categorias");
        
        rvCategorias = findViewById(R.id.rv_categorias);
    }

    @Override
    public void OnClick(Categoria categoria) {
        Intent intent = new Intent();
        intent.putExtra("categoriaSelecionada", categoria);
        setResult(RESULT_OK, intent);
        finish();
    }
}
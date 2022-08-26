package com.beatriz.olx_clone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.adapter.EstadoAdapter;
import com.beatriz.olx_clone.adapter.RegiaoAdapter;
import com.beatriz.olx_clone.helper.RegioesList;
import com.beatriz.olx_clone.helper.SPFiltro;

import java.util.ArrayList;
import java.util.List;

public class RegioesActivity extends AppCompatActivity implements RegiaoAdapter.OnClickListener {

    private RegiaoAdapter regiaoAdapter;
    private RecyclerView rvRegioes;
    private Boolean acesso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regioes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            acesso = bundle.getBoolean("filtros");
        }

        iniciarComponentes();
        configCliques();
        configRv();
    }

    private void configRv() {
        rvRegioes.setLayoutManager(new LinearLayoutManager(this));
        rvRegioes.setHasFixedSize(true);
        regiaoAdapter = new RegiaoAdapter(RegioesList.getList(SPFiltro.getFiltro(this).getEstado().getUf()), this);
        rvRegioes.setAdapter(regiaoAdapter);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Selecione a região");

        rvRegioes = findViewById(R.id.rv_regioes);
    }

    @Override
    public void OnClick(String regiao) {
        if (!regiao.equalsIgnoreCase("Todas as regiões")) {
            SPFiltro.setFiltro(this, "ddd", regiao.substring(4, 6));
            SPFiltro.setFiltro(this, "regiao", regiao);
        } else {
            SPFiltro.setFiltro(this, "ddd", "");
            SPFiltro.setFiltro(this, "regiao", "");
        }

        if (acesso) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
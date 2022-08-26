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
import com.beatriz.olx_clone.helper.EstadosList;
import com.beatriz.olx_clone.helper.SPFiltro;
import com.beatriz.olx_clone.model.Estado;

public class EstadosActivity extends AppCompatActivity implements EstadoAdapter.OnClickListener {

    private RecyclerView rvEstados;
    private EstadoAdapter estadoAdapter;
    private Boolean acesso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            acesso = bundle.getBoolean("filtros");
        }

        iniciarComponentes();
        configRv();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void configRv() {
        rvEstados.setLayoutManager(new LinearLayoutManager(this));
        rvEstados.setHasFixedSize(true);

        estadoAdapter = new EstadoAdapter(EstadosList.getList(), this);
        rvEstados.setAdapter(estadoAdapter);
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Estados");

        rvEstados = findViewById(R.id.rv_estados);
    }

    @Override
    public void OnClick(Estado estado) {
        if (!estado.getNome().equals("Brasil")) {
            SPFiltro.setFiltro(this, "ufEstado", estado.getUf());
            SPFiltro.setFiltro(this, "nomeEstado", estado.getNome());

            if (acesso) {
                finish();
            } else {
                startActivity(new Intent(this, RegioesActivity.class));
            }
        } else {
            SPFiltro.setFiltro(this, "ufEstado", "");
            SPFiltro.setFiltro(this, "nomeEstado", "");
            finish();
        }
    }
}
package com.beatriz.olx_clone.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.SPFiltro;
import com.beatriz.olx_clone.model.Categoria;
import com.beatriz.olx_clone.model.Filtro;
import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.Locale;

public class FiltrosActivity extends AppCompatActivity {

    private Button btnRegioes;
    private Button btnEstados;
    private Button btnCategoria;

    private CurrencyEditText edtValorMin;
    private CurrencyEditText edtValorMax;

    private final int REQUEST_CATEGORIA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        iniciaComponentes();

        configCliques();

    }

    @Override
    protected void onStart() {
        super.onStart();

        configFiltros();
    }

    private void configFiltros(){
        Filtro filtro = SPFiltro.getFiltro(this);

        if(!filtro.getEstado().getNome().isEmpty()){
            btnEstados.setText(filtro.getEstado().getNome());

            btnRegioes.setVisibility(View.VISIBLE);
        }else {
            btnEstados.setText("Todos os estados");

            btnRegioes.setVisibility(View.GONE);
        }

        if(!filtro.getCategoria().isEmpty()){
            btnCategoria.setText(filtro.getCategoria());
        }else {
            btnCategoria.setText("Todas as categorias");
        }

        if(!filtro.getEstado().getRegiao().isEmpty()){
            btnRegioes.setText(filtro.getEstado().getRegiao());
        }else {
            btnRegioes.setText("Todas as regiões");
        }

        if(filtro.getValorMin() > 0){
            edtValorMin.setValue(filtro.getValorMin() * 100);
        }else {
            edtValorMin.setValue(0);
        }

        if(filtro.getValorMax() > 0){
            edtValorMax.setValue(filtro.getValorMax() * 100);
        }else {
            edtValorMax.setValue(0);
        }

    }

    private void configCliques(){
        findViewById(R.id.btn_limpar).setOnClickListener(v -> {
            SPFiltro.limparFiltros(this);
            finish();
        });
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        btnRegioes.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegioesActivity.class);
            intent.putExtra("filtros", true);
            startActivity(intent);
        });
        btnEstados.setOnClickListener(v -> {
            Intent intent = new Intent(this, EstadosActivity.class);
            intent.putExtra("filtros", true);
            startActivity(intent);
        });

        btnCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriasActivity.class);
            startActivityForResult(intent, REQUEST_CATEGORIA);
        });

        findViewById(R.id.btn_filtrar).setOnClickListener(v -> {
            recuperaValores();
            finish();
        });
    }

    private void recuperaValores(){
        String valorMin = String.valueOf(edtValorMin.getRawValue() / 100);
        String valorMax = String.valueOf(edtValorMax.getRawValue() / 100);

        SPFiltro.setFiltro(this, "valorMin", valorMin);
        SPFiltro.setFiltro(this, "valorMax", valorMax);
    }

    private void iniciaComponentes(){
        btnRegioes = findViewById(R.id.btn_regioes);
        btnEstados = findViewById(R.id.btn_estados);
        btnCategoria = findViewById(R.id.btn_categoria);

        edtValorMin = findViewById(R.id.edt_valor_min);
        edtValorMax = findViewById(R.id.edt_valor_max);

        edtValorMin.setLocale(new Locale("PT", "br"));
        edtValorMax.setLocale(new Locale("PT", "br"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CATEGORIA){
                Categoria categoriaSelecionada = (Categoria) data.getExtras().getSerializable("categoriaSelecionada");
                SPFiltro.setFiltro(this, "categoria", categoriaSelecionada.getNome());

                configFiltros();
            }
        }
    }
}
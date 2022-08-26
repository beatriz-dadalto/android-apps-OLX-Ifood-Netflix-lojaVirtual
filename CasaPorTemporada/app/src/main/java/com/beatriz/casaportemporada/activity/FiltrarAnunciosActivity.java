package com.beatriz.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.model.Filtro;

public class FiltrarAnunciosActivity extends AppCompatActivity {

    private TextView textQuarto;
    private TextView textBanheiro;
    private TextView textGaragem;

    private SeekBar sbQuarto;
    private SeekBar sbBanheiro;
    private SeekBar sbGaragem;

    private int quantidadeQuarto;
    private int quantidadeBanheiro;
    private int quantidadeGaragem;

    private Filtro filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_anuncios);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            filtro = (Filtro) bundle.getSerializable("filtro");
            if(filtro != null) {
                configFiltros();
            }
        }

        configCliques();
        configSb(); //seekbar
    }

    private void configFiltros() {
        sbQuarto.setProgress(filtro.getQuantidadeQuarto());
        sbBanheiro.setProgress(filtro.getQuantidadeBanheiro());
        sbGaragem.setProgress(filtro.getQuantidadeGaragem());

        textQuarto.setText(filtro.getQuantidadeQuarto() + " quartos ou mais");
        textBanheiro.setText(filtro.getQuantidadeBanheiro() +" banheiros ou mais");
        textGaragem.setText(filtro.getQuantidadeGaragem() + " garagens ou mais");

        quantidadeQuarto = filtro.getQuantidadeQuarto();
        quantidadeBanheiro = filtro.getQuantidadeBanheiro();
        quantidadeGaragem = filtro.getQuantidadeGaragem();
    }

    public void filtrar(View view) {
        if(filtro == null) {
            filtro = new Filtro();
        }

        if(quantidadeQuarto > 0) filtro.setQuantidadeQuarto(quantidadeQuarto);
        if(quantidadeBanheiro > 0) filtro.setQuantidadeBanheiro(quantidadeBanheiro);
        if(quantidadeGaragem > 0) filtro.setQuantidadeGaragem(quantidadeGaragem);

        if(quantidadeQuarto > 0 || quantidadeBanheiro > 0 || quantidadeGaragem > 0) {
            Intent intent = new Intent();
            intent.putExtra("filtro", filtro);
            setResult(RESULT_OK, intent);
            finish();
        }

        finish();
    }

    private void configSb() {
        sbQuarto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textQuarto.setText(progress + " quartos ou mais");
                quantidadeQuarto = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbBanheiro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textBanheiro.setText(progress + " banheiros ou mais");
                quantidadeBanheiro = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbGaragem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textGaragem.setText(progress + " garagens ou mais");
                quantidadeGaragem = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void limparFiltro(View view) {
        quantidadeQuarto = 0;
        quantidadeBanheiro = 0;
        quantidadeGaragem = 0;

        finish();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        TextView textTituloToolbar = findViewById(R.id.text_titulo);
        textTituloToolbar.setText("Filtrar");

        textQuarto = findViewById(R.id.text_quarto);
        textBanheiro = findViewById(R.id.text_banheiro);
        textGaragem = findViewById(R.id.text_garagem);
        sbQuarto = findViewById(R.id.sb_quarto);
        sbBanheiro = findViewById(R.id.sb_banheiro);
        sbGaragem = findViewById(R.id.sb_garagem);
    }
}
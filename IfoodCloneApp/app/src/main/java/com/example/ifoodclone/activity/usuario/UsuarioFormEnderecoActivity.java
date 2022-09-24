package com.example.ifoodclone.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Endereco;

public class UsuarioFormEnderecoActivity extends AppCompatActivity {

    private EditText edt_logradouro;
    private EditText edt_bairro;
    private EditText edt_municipio;
    private EditText edt_referencia;

    private ImageButton ib_salvar;
    private ProgressBar progressBar;
    private TextView text_toolbar;

    private Endereco endereco;
    private Boolean novoEndereco = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_form_endereco);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            endereco = (Endereco) bundle.getSerializable("enderecoSelecionado");
            configDados();
        }

        configCliques();

    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validaDados());
    }

    private void configSalvar(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
            ib_salvar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            ib_salvar.setVisibility(View.VISIBLE);
        }
    }

    private void validaDados() {
        String logradouro = edt_logradouro.getText().toString().trim();
        String bairro = edt_bairro.getText().toString().trim();
        String municipio = edt_municipio.getText().toString().trim();
        String referencia = edt_referencia.getText().toString().trim();

        if (!logradouro.isEmpty()) {
            if (!referencia.isEmpty()) {
                if (!bairro.isEmpty()) {
                    if (!municipio.isEmpty()) {

                        configSalvar(true);

                        if (endereco == null) endereco = new Endereco();
                        endereco.setLogradouro(logradouro);
                        endereco.setBairro(bairro);
                        endereco.setMunicipio(municipio);
                        endereco.setReferencia(referencia);

                        endereco.salvar();

                        if(novoEndereco){
                            finish();
                        }else {
                            ocultarTeclado();
                            Toast.makeText(this, "Endereço salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        }

                        configSalvar(false);

                    } else {
                        edt_municipio.requestFocus();
                        edt_municipio.setError("Informe o município.");
                    }
                } else {
                    edt_bairro.requestFocus();
                    edt_bairro.setError("Informe o bairro.");
                }
            } else {
                edt_referencia.requestFocus();
                edt_referencia.setError("Informe uma referência.");
            }
        } else {
            edt_logradouro.requestFocus();
            edt_logradouro.setError("Informe o endereço");
        }
    }

    private void configDados() {
        edt_logradouro.setText(endereco.getLogradouro());
        edt_bairro.setText(endereco.getBairro());
        edt_municipio.setText(endereco.getMunicipio());
        edt_referencia.setText(endereco.getReferencia());

        novoEndereco = false;
        text_toolbar.setText("Edição");
    }

    private void iniciaComponentes() {
        text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo endereço");

        edt_logradouro = findViewById(R.id.edt_logradouro);
        edt_bairro = findViewById(R.id.edt_bairro);
        edt_municipio = findViewById(R.id.edt_municipio);
        edt_referencia = findViewById(R.id.edt_referencia);

        ib_salvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);

        configSalvar(false);
    }

    private void ocultarTeclado(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                ib_salvar.getWindowToken(), 0
        );
    }

}
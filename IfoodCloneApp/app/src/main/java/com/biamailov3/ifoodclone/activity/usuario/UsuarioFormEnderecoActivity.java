package com.biamailov3.ifoodclone.activity.usuario;

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

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Endereco;

public class UsuarioFormEnderecoActivity extends AppCompatActivity {

    private EditText edtLogradouro;
    private EditText edtBairro;
    private EditText edtMunicipio;
    private EditText edtReferencia;
    private TextView textToolbar;

    private ImageButton ibSalvar;
    private ProgressBar progressBar;

    private Endereco endereco;
    private Boolean novoEndereco = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_form_endereco);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            endereco = (Endereco) bundle.getSerializable("enderecoSelecionado");
            configDados();
        }

        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_salvar).setOnClickListener(view -> validarDados());
    }

    private void configSalvar(boolean showProgressBar) {
        if (showProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
            ibSalvar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            ibSalvar.setVisibility(View.VISIBLE);
        }
    }

    private void validarDados() {
        String logradouro = edtLogradouro.getText().toString().trim();
        String bairro = edtBairro.getText().toString().trim();
        String municipio = edtMunicipio.getText().toString().trim();
        String referencia = edtReferencia.getText().toString().trim();

        if (!logradouro.isEmpty()) {
            if (!referencia.isEmpty()) {
                if (!bairro.isEmpty()) {
                    if (!municipio.isEmpty()) {

                        configSalvar(true);
                        ocultarTeclado();

                        if (endereco == null) endereco = new Endereco();
                        endereco.setLogradouro(logradouro);
                        endereco.setBairro(bairro);
                        endereco.setMunicipio(municipio);
                        endereco.setReferencia(referencia);

                        endereco.salvar();

                        if (novoEndereco) {
                            finish();
                        } else {
                            ocultarTeclado();
                            Toast.makeText(this, "Endereço salvo!", Toast.LENGTH_SHORT).show();
                        }

                        configSalvar(false);

                    } else {
                        edtMunicipio.requestFocus();
                        edtMunicipio.setError("Informe o município");
                    }
                } else {
                    edtBairro.requestFocus();
                    edtBairro.setError("Informe o bairro");
                }
            } else {
                edtReferencia.requestFocus();
                edtReferencia.setError("Digite uma referência");
            }
        } else {
            edtLogradouro.requestFocus();
            edtLogradouro.setError("Informe o endereço");
        }
    }

    private void configDados() {
        edtLogradouro.setText(endereco.getLogradouro());
        edtBairro.setText(endereco.getBairro());
        edtMunicipio.setText(endereco.getMunicipio());
        edtReferencia.setText(endereco.getReferencia());

        novoEndereco = false;
        textToolbar.setText("Edição");
    }

    private void iniciarComponentes() {
        textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Meu endereço");

        edtLogradouro = findViewById(R.id.edt_logradouro);
        edtBairro = findViewById(R.id.edt_bairro);
        edtMunicipio = findViewById(R.id.edt_municipio);
        edtReferencia = findViewById(R.id.edt_referencia);
        ibSalvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);

        configSalvar(false);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ibSalvar.getWindowToken(), 0);
    }
}
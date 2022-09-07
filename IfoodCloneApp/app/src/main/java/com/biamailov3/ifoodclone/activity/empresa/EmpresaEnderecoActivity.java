package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.Entrega;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EmpresaEnderecoActivity extends AppCompatActivity {

    private EditText edtLogradouro;
    private EditText edtBairro;
    private EditText edtMunicipio;

    private ImageButton ibSalvar;
    private ProgressBar progressBar;

    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_endereco);

        iniciarComponentes();
        configCliques();
        recuperarEndereco();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_salvar).setOnClickListener(view -> validarDados());
    }

    private void recuperarEndereco() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    endereco = snapshot.getValue(Endereco.class);
                    configDados();
                } else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void configDados() {
        edtLogradouro.setText(endereco.getLogradouro());
        edtBairro.setText(endereco.getBairro());
        edtMunicipio.setText(endereco.getMunicipio());

        configSalvar(false);
    }

    private void validarDados() {
        String logradouro = edtLogradouro.getText().toString().trim();
        String bairro = edtBairro.getText().toString().trim();
        String municipio = edtMunicipio.getText().toString().trim();

        if (!logradouro.isEmpty()) {
            if (!bairro.isEmpty()) {
                if (!municipio.isEmpty()) {

                    configSalvar(true);
                    ocultarTeclado();

                    if (endereco == null) endereco = new Endereco();
                    endereco.setLogradouro(logradouro);
                    endereco.setBairro(bairro);
                    endereco.setMunicipio(municipio);

                    endereco.salvar();

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
            edtLogradouro.requestFocus();
            edtLogradouro.setError("Informe o endereço");
        }
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Meu endereço");

        edtLogradouro = findViewById(R.id.edt_logradouro);
        edtBairro = findViewById(R.id.edt_bairro);
        edtMunicipio = findViewById(R.id.edt_municipio);
        ibSalvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtLogradouro.getWindowToken(), 0);
    }
}
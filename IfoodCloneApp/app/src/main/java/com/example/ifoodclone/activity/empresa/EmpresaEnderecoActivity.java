package com.example.ifoodclone.activity.empresa;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresaEnderecoActivity extends AppCompatActivity {

    private EditText edt_logradouro;
    private EditText edt_bairro;
    private EditText edt_municipio;

    private ImageButton ib_salvar;
    private ProgressBar progressBar;

    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_endereco);

        iniciaComponentes();

        configCliques();

        recuperaEndereco();

    }

    private void recuperaEndereco(){
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        endereco = ds.getValue(Endereco.class);
                    }
                    configDados();
                }else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configSalvar(boolean progress){
        if(progress){
            progressBar.setVisibility(View.VISIBLE);
            ib_salvar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            ib_salvar.setVisibility(View.VISIBLE);
        }
    }

    private void configDados(){
        edt_logradouro.setText(endereco.getLogradouro());
        edt_bairro.setText(endereco.getBairro());
        edt_municipio.setText(endereco.getMunicipio());

        configSalvar(false);
    }

    private void validaDados() {
        String logradouro = edt_logradouro.getText().toString().trim();
        String bairro = edt_bairro.getText().toString().trim();
        String municipio = edt_municipio.getText().toString().trim();

        if (!logradouro.isEmpty()) {
            if (!bairro.isEmpty()) {
                if (!municipio.isEmpty()) {

                    configSalvar(true);

                    if (endereco == null) endereco = new Endereco();
                    endereco.setLogradouro(logradouro);
                    endereco.setBairro(bairro);
                    endereco.setMunicipio(municipio);
                    endereco.salvar();

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
            edt_logradouro.requestFocus();
            edt_logradouro.setError("Informe o endereço");
        }

    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validaDados());
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Meu endereço");

        edt_logradouro = findViewById(R.id.edt_logradouro);
        edt_bairro = findViewById(R.id.edt_bairro);
        edt_municipio = findViewById(R.id.edt_municipio);

        ib_salvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }

}
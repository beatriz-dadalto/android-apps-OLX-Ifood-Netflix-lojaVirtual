package com.beatriz.olx_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

public class EnderecoActivity extends AppCompatActivity {

    private MaskEditText edtCEP;
    private EditText edtUF;
    private EditText edtMunicipio;
    private EditText edtBairro;
    private ProgressBar progressBar;

    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

        iniciarComponentes();
        recuperarEndereco();
        configCliques();
    }

    public void validarDados(View view) {
        String cep = edtCEP.getMasked();
        String uf = edtUF.getText().toString();
        String municipio = edtMunicipio.getText().toString();
        String bairro = edtBairro.getText().toString();

        if (!cep.isEmpty()) {
            if (cep.length() == 9) {
                if (!uf.isEmpty()) {
                    if (!municipio.isEmpty()) {
                        if (!bairro.isEmpty()) {

                            progressBar.setVisibility(View.VISIBLE);
                            if (endereco == null) endereco = new Endereco();
                            endereco.setCep(cep);
                            endereco.setUf(uf);
                            endereco.setMunicipio(municipio);
                            endereco.setBairro(bairro);
                            String idUser = FirebaseHelper.getIdFirebase();
                            endereco.salvar(idUser, getBaseContext(), progressBar);

                        } else {
                            edtBairro.requestFocus();
                            edtBairro.setError("Digite o Bairro.");
                        }
                    } else {
                        edtMunicipio.requestFocus();
                        edtMunicipio.setError("Digite o Município.");
                    }
                } else {
                    edtUF.requestFocus();
                    edtUF.setError("Digite o UF.");
                }
            } else {
                edtCEP.requestFocus();
                edtCEP.setError("Informe um CEP válido");
            }
        } else {
            edtCEP.requestFocus();
            edtCEP.setError("Digite o CEP.");
        }
    }

    public void recuperarEndereco() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    endereco = snapshot.getValue(Endereco.class);
                    preencherEndereco(endereco);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void preencherEndereco(Endereco endereco) {
        edtCEP.setText(endereco.getCep());
        edtUF.setText(endereco.getUf());
        edtMunicipio.setText(endereco.getMunicipio());
        edtBairro.setText(endereco.getBairro());

        progressBar.setVisibility(View.GONE);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Endereço");

        edtCEP = findViewById(R.id.edt_cep);
        edtUF = findViewById(R.id.edt_uf);
        edtMunicipio = findViewById(R.id.edt_municipio);
        edtBairro = findViewById(R.id.edt_bairro);
        progressBar = findViewById(R.id.progressBar);
    }
}
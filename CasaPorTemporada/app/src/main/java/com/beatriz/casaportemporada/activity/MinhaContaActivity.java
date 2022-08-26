package com.beatriz.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MinhaContaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editTelefone;
    private EditText editEmail;
    private ProgressBar progressBar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        
        iniciarComponentes();
        recuperarDados();
        configCliques();
    }

    private void recuperarDados() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        editNome.setText(usuario.getNome());
        editTelefone.setText(usuario.getTelefone());
        editEmail.setText(usuario.getEmail());
        progressBar.setVisibility(View.GONE);
    }

    public void configCliques() {
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validarDados());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.btn_deslogar).setOnClickListener(v -> {
            FirebaseHelper.getAuth().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void validarDados() {
        String nome = editNome.getText().toString();
        String telefone = editTelefone.getText().toString();

        if(!nome.isEmpty()) {
            if(!telefone.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                usuario.setNome(nome);
                usuario.setTelefone(telefone);
                usuario.salvar();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                editTelefone.requestFocus();
                editTelefone.setError("Digite seu telefone");
            }
        } else {
            editNome.requestFocus();
            editNome.setError("Digite seu nome");
        }
    }

    private void iniciarComponentes() {
        TextView textTituloToolbar = findViewById(R.id.text_titulo);
        textTituloToolbar.setText("Minha Conta");

        editNome = findViewById(R.id.edit_nome);
        editTelefone = findViewById(R.id.edit_telefone);
        editEmail = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progressBar);
    }
}
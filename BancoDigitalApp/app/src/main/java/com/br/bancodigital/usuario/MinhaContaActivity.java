package com.br.bancodigital.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bancodigital.R;
import com.br.bancodigital.app.MainActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DatabaseReference;

public class MinhaContaActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private ProgressBar progressBar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        configToolbar();
        iniciaComponentes();
        configDados();
    }

    public void validaDados(View view) {
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();

        if (!nome.isEmpty()) {
            if (!telefone.isEmpty()) {

                ocultarTeclado();
                progressBar.setVisibility(View.VISIBLE);

                usuario.setNome(nome);
                usuario.setTelefone(telefone);

                salvarDadosUsuario();

            } else {
                edtTelefone.requestFocus();
                edtTelefone.setError("Informe o telefone");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Informe o nome");
        }
    }

    private void salvarDadosUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(usuario.getId());
        usuarioRef.setValue(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Salvo com sucesso! \uD83E\uDD29", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Não foi possível salvar as informações", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void configDados() {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        edtNome.setText(usuario.getNome());
        edtTelefone.setText(usuario.getTelefone());
        edtEmail.setText(usuario.getEmail());

        progressBar.setVisibility(View.GONE);
    }

    private void iniciaComponentes() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        progressBar = findViewById(R.id.progressBar);
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Perfil");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
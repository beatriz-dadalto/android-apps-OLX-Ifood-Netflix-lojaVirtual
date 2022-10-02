package com.br.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.netflix.R;
import com.br.netflix.activity.MainActivity;
import com.br.netflix.helper.FirebaseHelper;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciaComponentes();
        configCliques();
    }

    private void validaDados() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                ocultarTeclado();
                progressBar.setVisibility(View.VISIBLE);
                cadastroFirebase(email, senha);

            } else {
                edtSenha.requestFocus();
                edtSenha.setError("Informe uma senha.");
            }
        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Informe um e-mail.");
        }

    }

    private void cadastroFirebase(String email, String senha) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configCliques() {
        findViewById(R.id.btnLogin).setOnClickListener(view -> finish());

        findViewById(R.id.btnCadastro).setOnClickListener(view -> validaDados());
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void iniciaComponentes() {
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        progressBar = findViewById(R.id.progressBar);
    }

}
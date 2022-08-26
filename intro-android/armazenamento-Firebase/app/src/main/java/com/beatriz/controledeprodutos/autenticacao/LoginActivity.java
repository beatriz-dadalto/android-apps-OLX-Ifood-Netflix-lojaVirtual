package com.beatriz.controledeprodutos.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.controledeprodutos.R;
import com.beatriz.controledeprodutos.activity.MainActivity;
import com.beatriz.controledeprodutos.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextView textCriarConta;
    private EditText editSenha;
    private EditText editEmail;
    private TextView recuperarConta;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();
        configCliques();
    }

    public void logar(View view) {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                validarLogin(email, senha);
            } else {
                editSenha.requestFocus();
                editSenha.setError("Informe a senha");
            }
        } else {
            editEmail.requestFocus();
            editEmail.setError("Informe seu email");
        }
    }

    private void validarLogin(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private void configCliques() {
        textCriarConta.setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
        recuperarConta.setOnClickListener(view -> startActivity(new Intent(this, RecuperarContaActivity.class)));
    }

    private void iniciarComponentes() {
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        textCriarConta = findViewById(R.id.text_criar_conta);
        recuperarConta = findViewById(R.id.text_recuperar_conta);
        progressBar = findViewById(R.id.progressBar);
    }
}
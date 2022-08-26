package com.beatriz.casaportemporada.activity.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.activity.FormAnuncioActivity;
import com.beatriz.casaportemporada.activity.MainActivity;
import com.beatriz.casaportemporada.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configCliques();
        iniciarComponentes();
    }

    private void configCliques() {
        findViewById(R.id.text_criar_conta).setOnClickListener(view ->
                startActivity(new Intent(this, CriarContaActivity.class)));
        findViewById(R.id.text_recuperar_conta).setOnClickListener(v ->
                startActivity(new Intent(this, RecuperarContaActivity.class)));
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    public void validarDados(View view) {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if(!email.isEmpty()) {
            if(!senha.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                logar(email, senha);
            } else {
                editSenha.requestFocus();
                editSenha.setError("Digite a senha");
            }
        } else {
            editEmail.requestFocus();
            editEmail.setError("Digite a senha");
        }
    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarComponentes() {
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);
    }
}
package com.beatriz.olx_clone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.activity.MainActivity;
import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();
    }

    public void validarDados(View view){
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        
        if(!email.isEmpty()) {
            if(!senha.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                logar(email, senha);
            } else {
                edtSenha.requestFocus();
                edtSenha.setError("Digite sua senha.");
            }
        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Digite seu email.");
        }
    }

    // quando clicar em cadastra-se
    public void criarConta(View view) {
        startActivity(new Intent(this, CriarContaActivity.class));
    }

    // quando clicar em esqueceu a senha?
    public void recuperarSenha(View view) {
        startActivity(new Intent(this, RecuperarSenhaActivity.class));
    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               startActivity(new Intent(this, MainActivity.class));
               finish();
           } else {
               // pegar errors
               //Log.i("INFOTESTE", "logar: " + task.getException().getMessage());

               String erro = FirebaseHelper.validarErros(task.getException().getMessage());
               Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
           }
           progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Login");

        edtEmail = findViewById(R.id.edt_email);
        edtSenha = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }
}
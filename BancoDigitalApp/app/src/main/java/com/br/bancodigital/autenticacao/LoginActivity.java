package com.br.bancodigital.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.bancodigital.app.MainActivity;
import com.br.bancodigital.R;
import com.br.bancodigital.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private ProgressBar progressBar;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciaComponentes();
        configCliques();
    }

    private void configCliques() {
        btnLogin.setOnClickListener(view -> validaDados());
    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void validaDados() {
        ocultarTeclado();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                progressBar.setVisibility(View.VISIBLE);

                logar(email, senha);

            } else {
                edtSenha.requestFocus();
                edtSenha.setError("Informe a senha");
            }
        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Informe o email");
        }
    }

    public void criarConta(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void recuperarConta(View view) {
        startActivity(new Intent(this, RecuperaContaActivity.class));
    }

    private void iniciaComponentes() {
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
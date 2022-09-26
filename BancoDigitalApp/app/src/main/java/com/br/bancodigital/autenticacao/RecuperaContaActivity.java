package com.br.bancodigital.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.bancodigital.R;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Usuario;

public class RecuperaContaActivity extends AppCompatActivity {

    private EditText edtEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_conta);

        iniciaComponentes();
    }

    private void iniciaComponentes() {
        edtEmail = findViewById(R.id.edtEmail);
        progressBar = findViewById(R.id.progressBar);
    }

    public void validaDados(View view) {
        String email = edtEmail.getText().toString().trim();

        if (!email.isEmpty()) {
            ocultarTeclado();
            progressBar.setVisibility(View.VISIBLE);

            recuperarConta(email);

        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Informe o email");
        }
    }

    private void recuperarConta(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Acesse seu email. Enviamos um link para alterar sua senha.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
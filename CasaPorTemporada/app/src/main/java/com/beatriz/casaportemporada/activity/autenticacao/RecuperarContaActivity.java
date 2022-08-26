package com.beatriz.casaportemporada.activity.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.helper.FirebaseHelper;

public class RecuperarContaActivity extends AppCompatActivity {

    private EditText editEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta);

        configCliques();
        iniciarComponentes();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        editEmail = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progressBar);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Recuperar Conta");
    }

    public void validarDados(View view) {
        String email = editEmail.getText().toString();

        if(!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            recuperarSenha(email);
        } else {
            editEmail.requestFocus();
            editEmail.setError("Digite o email");
        }
    }

    private void recuperarSenha(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "Email enviado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}
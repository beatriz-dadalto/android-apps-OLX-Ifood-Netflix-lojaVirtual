package com.beatriz.controledeprodutos.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beatriz.controledeprodutos.R;
import com.beatriz.controledeprodutos.helper.FirebaseHelper;

public class RecuperarContaActivity extends AppCompatActivity {

    private EditText editEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta);

        iniciarComponentes();
    }

    public void recuperarSenha(View view) {
        String email = editEmail.getText().toString();
        if (!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            enviarEmail(email);
        } else {
            editEmail.requestFocus();
            editEmail.setError("Informe o email");
        }
    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciarComponentes() {
        editEmail = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progressBar);
    }
}
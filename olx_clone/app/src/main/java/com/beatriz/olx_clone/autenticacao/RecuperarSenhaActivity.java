package com.beatriz.olx_clone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.FirebaseHelper;

public class RecuperarSenhaActivity extends AppCompatActivity {
    
    private EditText edtEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        
        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    public void validarDados(View view){
        String email = edtEmail.getText().toString();

        if(!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            enviarEmail(email);
        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Digite seu email.");
        }
    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(
                email
        ).addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               Toast.makeText(this, "Sucesso! Acesse seu email agora.", Toast.LENGTH_SHORT).show();
           } else {
               // pegar erros
               //Log.i("INFOTESTE", " logar: " + task.getException().getMessage());

               String erro = FirebaseHelper.validarErros(task.getException().getMessage());
               Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
           }
           progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Recuperar Senha");

        edtEmail = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
    }
}
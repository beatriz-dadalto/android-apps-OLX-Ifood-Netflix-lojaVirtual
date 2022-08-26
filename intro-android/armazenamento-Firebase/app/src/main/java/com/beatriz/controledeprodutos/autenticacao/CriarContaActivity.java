package com.beatriz.controledeprodutos.autenticacao;

import androidx.annotation.NonNull;
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
import com.beatriz.controledeprodutos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CriarContaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    public void validarDados(View view) {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if(!nome.isEmpty()) {
            if(!email.isEmpty()) {
                if(!senha.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    salvarCadastro(usuario);

                } else {
                    editSenha.requestFocus();
                    editSenha.setError("Informe sua senha");
                }
            } else {
                editEmail.requestFocus();
                editEmail.setError("Informe seu email");
            }
        } else {
            editNome.requestFocus();
            editNome.setError("Informe seu nome");
        }
    }

    private void salvarCadastro(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String id = task.getResult().getUser().getUid();
                usuario.setId(id);

                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        });
    }

    private void iniciarComponentes() {
        editNome = findViewById(R.id.edit_nome);
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);

        TextView textTitulo = findViewById(R.id.text_titulo);
        textTitulo.setText("Criar Conta");
    }
}
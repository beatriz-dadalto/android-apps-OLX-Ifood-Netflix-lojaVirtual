package com.beatriz.casaportemporada.activity.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.activity.MainActivity;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Usuario;

public class CriarContaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editTelefone;
    private EditText editSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        configCliques();
        iniciarComponentes();
    }

    public void validarDados(View view) {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String telefone = editTelefone.getText().toString();
        String senha = editSenha.getText().toString();

        if(!nome.isEmpty()) {
            if(!email.isEmpty()) {
                if(!telefone.isEmpty()) {
                    if(!senha.isEmpty()) {

                        progressBar.setVisibility(View.VISIBLE);
                        Usuario usuario = new Usuario(nome,email,telefone,senha);
                        cadastrarUsuario(usuario);

                        // apos salvar. fechar a tela e abrir a tela home do app
                        finish();
                        startActivity(new Intent(this, MainActivity.class));

                    } else {
                        editSenha.requestFocus();
                        editSenha.setError("Digite sua senha");
                    }
                } else {
                    editTelefone.requestFocus();
                    editTelefone.setError("Digite seu telefone");
                }
            } else {
                editEmail.requestFocus();
                editEmail.setError("Digite seu email");
            }
        } else {
            editNome.requestFocus();
            editNome.setError("Digite seu nome");
        }
    }

    private void cadastrarUsuario(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String idUser = task.getResult().getUser().getUid();
                usuario.setId(idUser);
                usuario.salvar();
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        editNome = findViewById(R.id.edit_nome);
        editEmail = findViewById(R.id.edit_email);
        editTelefone = findViewById(R.id.edit_telefone);
        editSenha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Crie sua Conta");
    }
}
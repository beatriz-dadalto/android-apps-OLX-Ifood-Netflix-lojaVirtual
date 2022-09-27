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
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DatabaseReference;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private ProgressBar progressBar;
    private Button btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciaComponentes();
    }

    public void validaDados(View view) {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String confirmaSenha = edtConfirmaSenha.getText().toString().trim();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!telefone.isEmpty()) {
                    if (!senha.isEmpty()) {
                        if (!confirmaSenha.isEmpty()) {
                            if (senha.equals(confirmaSenha)) {

                                progressBar.setVisibility(View.VISIBLE);

                                Usuario usuario = new Usuario();
                                usuario.setNome(nome);
                                usuario.setEmail(email);
                                usuario.setTelefone(telefone);
                                usuario.setSenha(senha);
                                usuario.setSaldo(0);

                                cadastrarUsuario(usuario);

                            } else {
                                edtSenha.setError("Senhas diferentes");
                                edtConfirmaSenha.setError("Senhas diferentes");
                            }
                        } else {
                            edtConfirmaSenha.requestFocus();
                            edtConfirmaSenha.setError("Confirme a senha");
                        }
                    } else {
                        edtSenha.requestFocus();
                        edtSenha.setError("Informe a senha");
                    }
                } else {
                    edtTelefone.requestFocus();
                    edtTelefone.setError("Informe o telefone");
                }
            } else {
                edtEmail.requestFocus();
                edtEmail.setError("Informe o email");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Informe o nome");
        }
    }

    private void cadastrarUsuario(Usuario usuario) {
        ocultarTeclado();
        FirebaseHelper.getAuth()
                .createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // recupera o id do user que acabou de se cadastrar
                        String id = task.getResult().getUser().getUid();
                        usuario.setId(id);

                        salvarDados(usuario);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarDados(Usuario usuario) {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(usuario.getId());
        usuarioRef.setValue(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciaComponentes() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmaSenha = findViewById(R.id.edtConfirmaSenha);
        progressBar = findViewById(R.id.progressBar);
        btnCriarConta = findViewById(R.id.btnCriarConta);
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
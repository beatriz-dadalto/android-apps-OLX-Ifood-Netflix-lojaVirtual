package com.beatriz.olx_clone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.activity.MainActivity;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Usuario;
import com.santalu.maskara.widget.MaskEditText;

public class CriarContaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private MaskEditText editTelefone;
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

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Criar Conta");

        editNome = findViewById(R.id.edt_nome);
        editEmail = findViewById(R.id.edt_email);
        editTelefone = findViewById(R.id.edt_telefone);
        editSenha = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);
    }

    public void validarDados(View view) {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String telefone = editTelefone.getMasked();
        String senha = editSenha.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!telefone.isEmpty()) {
                    if (telefone.length() == 15) {
                        if (!senha.isEmpty()) {

                            progressBar.setVisibility(View.VISIBLE);

                            Usuario usuario = new Usuario(nome, email, telefone, senha);

                            cadastrarUsuario(usuario);

                        } else {
                            editSenha.requestFocus();
                            editSenha.setError("Digite sua senha");
                        }
                    } else {
                        editTelefone.requestFocus();
                        editTelefone.setError("Digite um telefone válido");
                    }
                } else {
                    editTelefone.requestFocus();
                    editTelefone.setError("Informe um telefone");
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
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // retorna o id do user que o proprio firebase vai gerar
                        String id = task.getResult().getUser().getUid();
                        usuario.setId(id);
                        usuario.salvar(progressBar, getBaseContext());

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        // pegar erros
                        //Log.i("INFOTESTE", "logar: " + task.getException().getMessage());

                        String erro = FirebaseHelper.validarErros(task.getException().getMessage());
                        Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
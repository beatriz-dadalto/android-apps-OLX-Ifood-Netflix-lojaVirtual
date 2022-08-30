package com.biamailov3.ifoodclone.activity.autenticacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaFinalizaCadastroActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaHomeActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioFinalizaCadastroActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioHomeActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Login;
import com.biamailov3.ifoodclone.model.TipoUsuario;
import com.biamailov3.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private ProgressBar progressBar;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.text_criar_conta).setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
    }

    public void validarDados(View view) {

        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                ocultarTeclado();
                progressBar.setVisibility(View.VISIBLE);

                logar(email, senha);
            } else {
                edtSenha.requestFocus();
                edtSenha.setText("Informe sua senha");
            }
        } else {
            edtEmail.requestFocus();
            edtEmail.setError("Informe seu email");
        }
    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // verificar se ja tem cadastro finalizado
                        // se sim vai para UsuarioHome
                        // se nao vai para UsuarioFinalizaCadastro
                        verificarCadastro(task.getResult().getUser().getUid());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        erroAutenticacao(FirebaseHelper.validaErros(task.getException().getMessage()));
                    }
                });
    }

    private void verificarCadastro(String idUser) {
        DatabaseReference loginRef = FirebaseHelper.getDatabaseReference()
                .child("login")
                .child(idUser);
        loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                login = snapshot.getValue(Login.class);

                verificarTipoAcesso(login);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificarTipoAcesso(Login login) {
        if (login.getTipo().equals(TipoUsuario.USUARIO)) {
            if (login.getAcesso()) {
                finish();
                startActivity(new Intent(getBaseContext(), UsuarioHomeActivity.class));
            } else {
                recuperarUsuario();
            }
        } else if (login.getTipo().equals(TipoUsuario.EMPRESA)){
            if (login.getAcesso()) {
                finish();
                startActivity(new Intent(getBaseContext(), EmpresaHomeActivity.class));
            } else {
                recuperarEmpresa();
            }
        }

    }

    private void recuperarUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(login.getId());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                if (usuario != null) {
                    finish();
                    Intent intent = new Intent(getBaseContext(), UsuarioFinalizaCadastroActivity.class);
                    intent.putExtra("login", login);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(login.getId());
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empresa empresa = snapshot.getValue(Empresa.class);
                if (empresa != null) {
                    finish();
                    Intent intent = new Intent(getBaseContext(), EmpresaFinalizaCadastroActivity.class);
                    intent.putExtra("login", login);
                    intent.putExtra("empresa", empresa);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Login");

        edtEmail = findViewById(R.id.edt_email);
        edtSenha = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
    }
}
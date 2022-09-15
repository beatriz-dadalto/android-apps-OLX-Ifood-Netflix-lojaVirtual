package com.biamailov3.ifoodclone.activity.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
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

public class SplashActivity extends AppCompatActivity {

    private Login login;
    private Usuario usuario;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        limparSQLite();

        verificarAutenticacao();
    }

    private void limparSQLite() {
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        itemPedidoDAO.limparCarrinho();
    }

    private void verificarAutenticacao() {
        if (FirebaseHelper.getAutenticado()) {
            verificarCadastro();
        } else {
            finish();
            startActivity(new Intent(this, UsuarioHomeActivity.class));
        }
    }

    private void verificarCadastro() {
        DatabaseReference loginRef = FirebaseHelper.getDatabaseReference()
                .child("login")
                .child(FirebaseHelper.getIdFirebase());
        loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                login = snapshot.getValue(Login.class);
                verificarAcessoTipoUsuario(login);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificarAcessoTipoUsuario(Login login) {
        if (login != null) {
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
}
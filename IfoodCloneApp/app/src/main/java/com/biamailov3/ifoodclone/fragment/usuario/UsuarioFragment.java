package com.biamailov3.ifoodclone.fragment.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioFinalizaCadastroActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Login;
import com.biamailov3.ifoodclone.model.TipoUsuario;
import com.biamailov3.ifoodclone.model.Usuario;

public class UsuarioFragment extends Fragment {

    private EditText edtEmail;
    private EditText edtSenha;
    private ProgressBar progressBar;
    private Button btnCriarConta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

        iniciarComponentes(view);
        configCliques();

        return view;
    }

    private void configCliques() {
        btnCriarConta.setOnClickListener(view -> validarDados());
    }

    private void validarDados() {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                ocultarTeclado();
                progressBar.setVisibility(View.VISIBLE);
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                criarConta(usuario);
            } else {
                edtSenha.requestFocus();
                edtSenha.setError("Informe sua senha");
            }
        } else {
            edtEmail.requestFocus();
            edtSenha.setError("Informe seu e-mail");
        }
    }

    private void criarConta(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
                // resgatar o id do user que acabou de ser criado no firebase
               String id = task.getResult().getUser().getUid();
               usuario.setId(id);
               usuario.salvar();

               Login login = new Login(id, TipoUsuario.USUARIO, false);
               login.salvar();

               // levar dados para UsuarioFinalizaCadastroActivity
               requireActivity().finish(); // fechar tela de cadastro
               Intent intent = new Intent(requireActivity(), UsuarioFinalizaCadastroActivity.class);
               intent.putExtra("login", login);
               intent.putExtra("usuario", usuario);
               startActivity(intent);
           } else {
               progressBar.setVisibility(View.GONE);
                erroAutenticacao(FirebaseHelper.validaErros(task.getException().getMessage()));
           }
        });
    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciarComponentes(View view) {
        edtEmail = view.findViewById(R.id.edt_email);
        edtSenha = view.findViewById(R.id.edt_senha);
        progressBar = view.findViewById(R.id.progressBar);
        btnCriarConta = view.findViewById(R.id.btn_criar_conta);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(btnCriarConta.getWindowToken(), 0);
    }
}
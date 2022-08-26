package com.beatriz.olx_clone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.activity.EnderecoActivity;
import com.beatriz.olx_clone.activity.MainActivity;
import com.beatriz.olx_clone.activity.PerfilActivity;
import com.beatriz.olx_clone.autenticacao.LoginActivity;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MinhaContaFragment extends Fragment {

    private TextView textConta;
    private TextView textUsuario;
    private ImageView imagemPerfil;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

        iniciarComponentes(view);
        configCliques(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        
        recuperarUsuario();
    }

    private void recuperarUsuario() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                    .child("usuarios")
                    .child(FirebaseHelper.getIdFirebase());
            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuario = snapshot.getValue(Usuario.class);
                    configData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configData() {
        textUsuario.setText(usuario.getNome());

        if (usuario.getImagemPerfil() != null) {
            imagemPerfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(usuario.getImagemPerfil())
                    .placeholder(R.drawable.loading)
                    .into(imagemPerfil);
        }
    }

    private void configCliques(View view) {
        view.findViewById(R.id.menu_perfil).setOnClickListener(v -> redirecionarUsuario(PerfilActivity.class));
        view.findViewById(R.id.menu_endereco).setOnClickListener(v -> redirecionarUsuario(EnderecoActivity.class));

        if (FirebaseHelper.getAutenticado()) {
            textConta.setText("Sair");
        } else {
            textConta.setText("Entrar");
        }

        textConta.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {
                FirebaseHelper.getAuth().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void redirecionarUsuario(Class<?> classNameActivity) {
        if (FirebaseHelper.getAutenticado()) {
            startActivity(new Intent(getActivity(), classNameActivity));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void iniciarComponentes(View view) {
        textConta = view.findViewById(R.id.text_conta);
        textUsuario = view.findViewById(R.id.text_usuario);
        imagemPerfil = view.findViewById(R.id.imagem_perfil);
    }


}
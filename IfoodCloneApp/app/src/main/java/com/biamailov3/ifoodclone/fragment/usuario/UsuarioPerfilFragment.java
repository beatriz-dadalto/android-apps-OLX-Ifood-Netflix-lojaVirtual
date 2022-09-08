package com.biamailov3.ifoodclone.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.autenticacao.CriarContaActivity;
import com.biamailov3.ifoodclone.activity.autenticacao.LoginActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioEnderecosActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioFavoritosActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioPerfilActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;

public class UsuarioPerfilFragment extends Fragment {

    private ConstraintLayout layoutLogado;
    private ConstraintLayout layoutDeslogado;
    private LinearLayout menuPerfil;
    private LinearLayout menuFavoritos;
    private LinearLayout menuEnderecos;
    private LinearLayout menuDeslogar;
    private Button btnEntrar;
    private Button btnCadastrar;
    private TextView textUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_perfil, container, false);

        iniciarComponentes(view);
        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        verificarAcesso();
    }

    private void configCliques() {
        btnEntrar.setOnClickListener(view -> startActivity(new Intent(requireActivity(), LoginActivity.class)));
        btnCadastrar.setOnClickListener(view -> startActivity(new Intent(requireActivity(), CriarContaActivity.class)));
        menuDeslogar.setOnClickListener(view -> deslogar());

        menuPerfil.setOnClickListener(view -> verificarAutenticacao(UsuarioPerfilActivity.class));
        menuFavoritos.setOnClickListener(view -> verificarAutenticacao(UsuarioFavoritosActivity.class));
        menuEnderecos.setOnClickListener(view -> verificarAutenticacao(UsuarioEnderecosActivity.class));
    }

    private void verificarAutenticacao(Class<?> clazzDestino) {
        if (FirebaseHelper.getAutenticado()) {
            startActivity(new Intent(requireActivity(), clazzDestino));
        } else {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
        }
    }

    private void deslogar() {
        FirebaseHelper.getAuth().signOut();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.menu_home);
    }

    private void verificarAcesso() {
        if (FirebaseHelper.getAutenticado()) {
            layoutDeslogado.setVisibility(View.GONE);
            layoutLogado.setVisibility(View.VISIBLE);
            menuDeslogar.setVisibility(View.VISIBLE);
            textUsuario.setText(FirebaseHelper.getAuth().getCurrentUser().getDisplayName());
        } else {
            layoutDeslogado.setVisibility(View.VISIBLE);
            layoutLogado.setVisibility(View.GONE);
            menuDeslogar.setVisibility(View.GONE);
        }
    }

    private void iniciarComponentes(View view) {
        layoutLogado = view.findViewById(R.id.layout_logado);
        layoutDeslogado = view.findViewById(R.id.layout_deslogado);
        menuPerfil = view.findViewById(R.id.menu_perfil);
        menuFavoritos = view.findViewById(R.id.menu_favoritos);
        menuEnderecos = view.findViewById(R.id.menu_enderecos);
        menuDeslogar = view.findViewById(R.id.menu_deslogar);
        btnEntrar = view.findViewById(R.id.btn_entrar);
        btnCadastrar = view.findViewById(R.id.btn_cadastrar);
        textUsuario = view.findViewById(R.id.text_usuario);
    }
}
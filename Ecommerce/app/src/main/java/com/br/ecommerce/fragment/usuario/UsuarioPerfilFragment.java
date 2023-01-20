package com.br.ecommerce.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.R;
import com.br.ecommerce.activity.usuario.MainActivityUsuario;
import com.br.ecommerce.activity.usuario.UsuarioEnderecoActivity;
import com.br.ecommerce.autenticacao.CadastroActivity;
import com.br.ecommerce.autenticacao.LoginActivity;
import com.br.ecommerce.databinding.FragmentUsuarioPerfilBinding;
import com.br.ecommerce.helper.FirebaseHelper;

public class UsuarioPerfilFragment extends Fragment {

    private FragmentUsuarioPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configCliques();
    }

    private void configCliques() {
        binding.btnEntrar.setOnClickListener(v -> startActivity(LoginActivity.class));
        binding.btnCadastrar.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CadastroActivity.class));
        });
        binding.btnMeusDados.setOnClickListener(v -> startActivity(LoginActivity.class));
        binding.btnEnderecos.setOnClickListener(v -> startActivity(UsuarioEnderecoActivity.class));

        binding.btnDeslogar.setOnClickListener(v -> {
            FirebaseHelper.getAuth().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireContext(), MainActivityUsuario.class));
        });
    }

    private void startActivity(Class<?> clazz) {
        if (FirebaseHelper.getAutenticado()) {
            startActivity(new Intent(requireContext(), clazz));
        } else {
            startActivity(new Intent(requireContext(), LoginActivity.class));
        }
    }
}
package com.biamailov3.ifoodclone.fragment.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaAddMaisActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaCategoriasActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaConfigActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaEnderecoActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaEntregasActivity;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaRecebimentosActivity;
import com.biamailov3.ifoodclone.activity.usuario.UsuarioHomeActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.squareup.picasso.Picasso;

public class EmpresaConfigFragment extends Fragment {

    private ImageView imgLogo;
    private TextView textEmpresa;
    private LinearLayout menuEmpresa;
    private LinearLayout menuCategorias;
    private LinearLayout menuRecebimentos;
    private LinearLayout menuAddMais;
    private LinearLayout menuEndereco;
    private LinearLayout menuEntregas;
    private LinearLayout menuDeslogar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_config, container, false);
        
        iniciarComponentes(view);
        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        configAcesso();
    }

    private void configAcesso() {
        Picasso.get().load(FirebaseHelper.getAuth().getCurrentUser().getPhotoUrl()).into(imgLogo);
        textEmpresa.setText(FirebaseHelper.getAuth().getCurrentUser().getDisplayName());
    }

    private void configCliques() {
        menuEmpresa.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaConfigActivity.class)));
        menuCategorias.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaCategoriasActivity.class)));
        menuRecebimentos.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaRecebimentosActivity.class)));
        menuAddMais.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaAddMaisActivity.class)));
        menuEndereco.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaEnderecoActivity.class)));
        menuEntregas.setOnClickListener(view -> startActivity(new Intent(requireContext(), EmpresaEntregasActivity.class)));
        menuDeslogar.setOnClickListener(view -> deslogar());
    }

    private void deslogar() {
        FirebaseHelper.getAuth().signOut();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), UsuarioHomeActivity.class));
    }

    private void iniciarComponentes(View view) {
        imgLogo = view.findViewById(R.id.img_logo);
        textEmpresa = view.findViewById(R.id.text_empresa);
        menuEmpresa = view.findViewById(R.id.menu_empresa);
        menuCategorias = view.findViewById(R.id.menu_categorias);
        menuRecebimentos = view.findViewById(R.id.menu_recebimentos);
        menuAddMais = view.findViewById(R.id.menu_add_mais);
        menuEndereco = view.findViewById(R.id.menu_endereco);
        menuEntregas = view.findViewById(R.id.menu_entregas);
        menuDeslogar = view.findViewById(R.id.menu_deslogar);
    }
}
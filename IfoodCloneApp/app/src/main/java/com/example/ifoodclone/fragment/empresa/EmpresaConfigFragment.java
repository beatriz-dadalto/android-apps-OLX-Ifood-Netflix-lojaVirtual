package com.example.ifoodclone.fragment.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.empresa.EmpresaAddMaisActivity;
import com.example.ifoodclone.activity.empresa.EmpresaCategoriasActivity;
import com.example.ifoodclone.activity.empresa.EmpresaConfigActivity;
import com.example.ifoodclone.activity.empresa.EmpresaEnderecoActivity;
import com.example.ifoodclone.activity.empresa.EmpresaEntregasActivity;
import com.example.ifoodclone.activity.empresa.EmpresaRecebimentosActivity;
import com.example.ifoodclone.activity.usuario.UsuarioHomeActivity;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.squareup.picasso.Picasso;


public class EmpresaConfigFragment extends Fragment {

    private ImageView img_logo;
    private TextView text_empresa;
    private LinearLayout menu_empresa;
    private LinearLayout menu_categorias;
    private LinearLayout menu_recebimentos;
    private LinearLayout menu_add_mais;
    private LinearLayout menu_endereco;
    private LinearLayout menu_entregas;
    private LinearLayout menu_deslogar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_config, container, false);

        iniciaComponentes(view);

        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        configAcesso();
    }

    private void configAcesso(){
        Picasso.get().load(FirebaseHelper.getAuth().getCurrentUser().getPhotoUrl()).into(img_logo);
        text_empresa.setText(FirebaseHelper.getAuth().getCurrentUser().getDisplayName());
    }

    private void configCliques(){
        menu_empresa.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaConfigActivity.class)));
        menu_categorias.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaCategoriasActivity.class)));
        menu_recebimentos.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaRecebimentosActivity.class)));
        menu_add_mais.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaAddMaisActivity.class)));
        menu_endereco.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaEnderecoActivity.class)));
        menu_entregas.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaEntregasActivity.class)));
        menu_deslogar.setOnClickListener(v -> deslogar());
    }

    private void deslogar(){
        FirebaseHelper.getAuth().signOut();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), UsuarioHomeActivity.class));
    }

    private void iniciaComponentes(View view){
        img_logo = view.findViewById(R.id.img_logo);
        text_empresa = view.findViewById(R.id.text_empresa);
        menu_empresa = view.findViewById(R.id.menu_empresa);
        menu_categorias = view.findViewById(R.id.menu_categorias);
        menu_recebimentos = view.findViewById(R.id.menu_recebimentos);
        menu_add_mais = view.findViewById(R.id.menu_add_mais);
        menu_endereco = view.findViewById(R.id.menu_endereco);
        menu_entregas = view.findViewById(R.id.menu_entregas);
        menu_deslogar = view.findViewById(R.id.menu_deslogar);
    }

}
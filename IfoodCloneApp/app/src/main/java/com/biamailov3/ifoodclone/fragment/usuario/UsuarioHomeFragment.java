package com.biamailov3.ifoodclone.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.DAO.EmpresaDAO;
import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaCardapioActivity;
import com.biamailov3.ifoodclone.activity.usuario.CarrinhoActivity;
import com.biamailov3.ifoodclone.adapter.EmpresaAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioHomeFragment extends Fragment implements EmpresaAdapter.OnClickListener {

    private EmpresaAdapter empresaAdapter;
    private List<Empresa> empresaList = new ArrayList<>();

    private RecyclerView rvEmpresas;
    private ProgressBar progressBar;
    private TextView textInfo;

    private TextView textQtdItemSacola;
    private TextView textTotalCarrinho;
    private ConstraintLayout llSacola;

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_home, container, false);

        itemPedidoDAO = new ItemPedidoDAO(getContext());
        empresaDAO = new EmpresaDAO(getContext());

        iniciarComponentes(view);
        configCliques();
        configRv();
        recuperarEmpresas();

        return view;
    }

    private void configCliques() {
        llSacola.setOnClickListener(view -> startActivity(new Intent(getActivity(), CarrinhoActivity.class)));
    }

    @Override
    public void onStart() {
        super.onStart();

        configSacola();
    }

    private void configRv() {
        rvEmpresas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEmpresas.setHasFixedSize(true);
        empresaAdapter = new EmpresaAdapter(empresaList, this, requireContext());
        rvEmpresas.setAdapter(empresaAdapter);
    }

    private void recuperarEmpresas() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas");
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    empresaList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Empresa empresa = ds.getValue(Empresa.class);
                        empresaList.add(empresa);
                    }
                } else {
                    textInfo.setText("Nenhuma empresa cadastrada");
                }

                progressBar.setVisibility(View.GONE);
                textInfo.setText("");
                Collections.reverse(empresaList);
                empresaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configSacola() {
        if (!itemPedidoDAO.getList().isEmpty()) {

            double totalPedido = itemPedidoDAO.getTotal() + empresaDAO.getEmpresa().getTaxaEntrega();

            llSacola.setVisibility(View.VISIBLE);
            textQtdItemSacola.setText(String.valueOf(itemPedidoDAO.getList().size()));
            textTotalCarrinho.setText(getString(R.string.text_valor, GetMask.getValor(totalPedido)));
        } else {
            llSacola.setVisibility(View.GONE);
            textQtdItemSacola.setText("");
            textTotalCarrinho.setText("");
        }
    }

    private void iniciarComponentes(View view) {
        rvEmpresas = view.findViewById(R.id.rv_empresas);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);

        textQtdItemSacola = view.findViewById(R.id.textQtdItemSacola);
        textTotalCarrinho = view.findViewById(R.id.textTotalCarrinho);
        llSacola = view.findViewById(R.id.ll_sacola);
    }

    @Override
    public void onClick(Empresa empresa) {
        Intent intent = new Intent(requireContext(), EmpresaCardapioActivity.class);
        intent.putExtra("empresaSelecionada", empresa);
        startActivity(intent);
    }
}
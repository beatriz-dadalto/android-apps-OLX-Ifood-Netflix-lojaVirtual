package com.br.ecommerce.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.UsuarioPedidosAdapter;
import com.br.ecommerce.autenticacao.LoginActivity;
import com.br.ecommerce.databinding.FragmentUsuarioPedidoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UsuarioPedidoFragment extends Fragment implements UsuarioPedidosAdapter.OnClickListener {

    private FragmentUsuarioPedidoBinding binding;
    private UsuarioPedidosAdapter usuarioPedidosAdapter;
    private List<Pedido> pedidoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUsuarioPedidoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configCliques();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (FirebaseHelper.getAutenticado()) {
            binding.btnLogin.setVisibility(View.GONE);
            configRv();
            recuperaPedidos();
        } else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.textInfo.setText("Você não está autenticado no app");
        }
    }

    private void configCliques() {
        binding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });
    }

    private void configRv() {
        binding.rvPedidos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPedidos.setHasFixedSize(true);
        usuarioPedidosAdapter = new UsuarioPedidosAdapter(pedidoList, requireContext(), this);
        binding.rvPedidos.setAdapter(usuarioPedidosAdapter);
    }

    private void recuperaPedidos() {
        DatabaseReference pedidosRef = FirebaseHelper.getDatabaseReference()
                .child("usuarioPedidos")
                .child(FirebaseHelper.getIdFirebase());
        pedidosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pedidoList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidoList.add(pedido);
                    }

                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhum pedido encontrado");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(pedidoList);
                usuarioPedidosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(Pedido pedido) {
        Toast.makeText(requireContext(), pedido.getPagamento(), Toast.LENGTH_SHORT).show();
    }
}
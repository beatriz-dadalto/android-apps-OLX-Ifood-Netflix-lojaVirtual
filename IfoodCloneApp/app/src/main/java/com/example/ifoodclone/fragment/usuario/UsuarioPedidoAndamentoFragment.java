package com.example.ifoodclone.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.autenticacao.LoginActivity;
import com.example.ifoodclone.activity.usuario.PedidoDetalheActivity;
import com.example.ifoodclone.adapter.UsuarioPedidoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Pedido;
import com.example.ifoodclone.model.StatusPedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioPedidoAndamentoFragment extends Fragment implements UsuarioPedidoAdapter.OnClickListener {

    private final List<Pedido> pedidoList = new ArrayList<>();
    private UsuarioPedidoAdapter usuarioPedidoAdapter;

    private RecyclerView rv_pedidos;
    private ProgressBar progressBar;
    private TextView text_info;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_pedido_andamento, container, false);

        iniciaComponentes(view);

        configRv();

        configClicks();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaPedidos();
    }

    private void configClicks() {
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), LoginActivity.class)));
    }

    private void recuperaPedidos() {
        if (FirebaseHelper.getAutenticado()) {
            btnLogin.setVisibility(View.GONE);

            DatabaseReference pedidoRef = FirebaseHelper.getDatabaseReference()
                    .child("usuarioPedidos")
                    .child(FirebaseHelper.getIdFirebase());
            pedidoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        pedidoList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Pedido pedido = ds.getValue(Pedido.class);
                            if (pedido != null) {
                                addPedidoList(pedido);
                            }
                        }
                        text_info.setText("");
                    } else {
                        text_info.setText("Nenhum pedido efetuado.");
                    }

                    progressBar.setVisibility(View.GONE);
                    Collections.reverse(pedidoList);
                    usuarioPedidoAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            text_info.setText("");
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void addPedidoList(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusPedido.CANCELADO_EMPRESA
                && pedido.getStatusPedido() != StatusPedido.CANCELADO_USUARIO
                && pedido.getStatusPedido() != StatusPedido.ENTREGUE) {
            pedidoList.add(pedido);
        }
    }

    private void configRv() {
        rv_pedidos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_pedidos.setHasFixedSize(true);
        usuarioPedidoAdapter = new UsuarioPedidoAdapter(pedidoList, getContext(), this);
        rv_pedidos.setAdapter(usuarioPedidoAdapter);
    }

    private void iniciaComponentes(View view) {
        rv_pedidos = view.findViewById(R.id.rv_pedidos);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    @Override
    public void OnClick(Pedido pedido, int rota) {
        if (rota == 0) {
            Toast.makeText(getContext(), "Ajuda.", Toast.LENGTH_SHORT).show();
        } else if (rota == 1) {
            Intent intent = new Intent(getActivity(), PedidoDetalheActivity.class);
            intent.putExtra("pedidoSelecionado", pedido);
            intent.putExtra("acesso", "usuario");
            startActivity(intent);
        }
    }
}
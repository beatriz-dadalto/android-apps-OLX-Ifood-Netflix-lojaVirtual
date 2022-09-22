package com.biamailov3.ifoodclone.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.EmpresaPedidoAdapter;
import com.biamailov3.ifoodclone.adapter.UsuarioPedidoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.StatusPedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioPedidoAndamentoFragment extends Fragment implements UsuarioPedidoAdapter.OnClickListener {

    private final List<Pedido> pedidoList = new ArrayList<>();
    private UsuarioPedidoAdapter usuarioPedidoAdapter;

    private RecyclerView rvPedidos;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_pedido_andamento, container, false);

        iniciarComponentes(view);
        configRv();
        recuperarPedidos();

        return view;
    }

    private void iniciarComponentes(View view) {
        rvPedidos = view.findViewById(R.id.rv_pedidos);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
    }

    private void recuperarPedidos() {
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
                        if (pedido != null) addPedidoList(pedido);
                    }

                } else {
                    textInfo.setText("Nenhum pedido efetuado.");
                }

                progressBar.setVisibility(View.GONE);
                usuarioPedidoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPedidoList(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusPedido.CANCELADO_EMPRESA ||
                pedido.getStatusPedido() != StatusPedido.CANCELADO_USUARIO ||
                pedido.getStatusPedido() != StatusPedido.ENTREGUE) {
            pedidoList.add(pedido);
        }
    }

    private void configRv() {
        rvPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPedidos.setHasFixedSize(true);
        usuarioPedidoAdapter = new UsuarioPedidoAdapter(pedidoList, getContext(), this);
        rvPedidos.setAdapter(usuarioPedidoAdapter);
    }

    @Override
    public void onClick(Pedido pedido, int rota) {
        if (rota == 0) {
            Toast.makeText(getContext(), "Ajuda", Toast.LENGTH_SHORT).show();
        } else if (rota == 1) {
            Toast.makeText(getContext(), "Detalhes do pedido", Toast.LENGTH_SHORT).show();
        }
    }
}
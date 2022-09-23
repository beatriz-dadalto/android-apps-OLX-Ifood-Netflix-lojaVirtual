package com.biamailov3.ifoodclone.fragment.empresa;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.usuario.PedidoDetalheActivity;
import com.biamailov3.ifoodclone.adapter.EmpresaPedidoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.StatusPedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaPedidoAndamentoFragment extends Fragment implements EmpresaPedidoAdapter.OnClickListener {

    private List<Pedido> pedidoList = new ArrayList<>();
    private EmpresaPedidoAdapter empresaPedidoAdapter;

    private RecyclerView rvPedidos;
    private ProgressBar progressBar;
    private TextView textInfo;

    private RadioGroup rgStatus;
    private RadioButton rbPendente;
    private RadioButton rbPreparacao;
    private RadioButton rbSaiuEntrega;
    private RadioButton rbEntregue;
    private RadioButton rbCancelado;
    private Button btnFechar;
    private Button btnSalvar;

    private AlertDialog dialog;

    private int status = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_pedido_andamento, container, false);
        
        iniciarComponentes(view);
        configRv();
        recuperarPedidos();
        
        return view;
    }

    private void recuperarPedidos() {
        DatabaseReference pedidoRef = FirebaseHelper.getDatabaseReference()
                .child("empresaPedidos")
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
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum pedido recebido");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(pedidoList);
                empresaPedidoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialogStatus(Pedido pedido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_status_pedido, null);
        builder.setView(view);

        rgStatus = view.findViewById(R.id.rg_status);
        rbPendente = view.findViewById(R.id.rb_pendente);
        rbPreparacao = view.findViewById(R.id.rb_preparacao);
        rbSaiuEntrega = view.findViewById(R.id.rb_saiu_entrega);
        rbEntregue = view.findViewById(R.id.rb_entregue);
        rbCancelado = view.findViewById(R.id.rb_cancelado);
        btnFechar = view.findViewById(R.id.btn_fechar);
        btnSalvar = view.findViewById(R.id.btn_salvar);

        configStatus(pedido);


        rgStatus.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            status = checkedId;
        });

        btnFechar.setOnClickListener(v -> dialog.dismiss());
        btnSalvar.setOnClickListener(v -> {
            atualizarStatus(pedido);
            dialog.dismiss();
        });

        dialog = builder.create();
        dialog.show();
    }

    private void atualizarStatus(Pedido pedido) {
        StatusPedido statusPedido;
        if (status == R.id.rb_preparacao) {
            statusPedido = StatusPedido.PREPARACAO;
        } else if (status == R.id.rb_saiu_entrega) {
            statusPedido = StatusPedido.SAIU_ENTREGA;
        } else if (status == R.id.rb_entregue) {
            statusPedido = StatusPedido.ENTREGUE;
        } else if (status == R.id.rb_cancelado) {
            statusPedido = StatusPedido.CANCELADO_EMPRESA;
        } else {
            statusPedido = StatusPedido.PENDENTE;
        }

        if (pedido.getStatusPedido() != statusPedido) {
            pedido.setStatusPedido(statusPedido);
            pedido.atualizar();
        }

        if (pedido.getStatusPedido() == StatusPedido.ENTREGUE ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_EMPRESA ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_USUARIO) {
            pedidoList.remove(pedido);
            empresaPedidoAdapter.notifyDataSetChanged();
        }
    }

    private void configStatus(Pedido pedido) {
        int id;
        switch (pedido.getStatusPedido()) {
            case ENTREGUE:
                id = R.id.rb_entregue;
                break;
            case PENDENTE:
                id = R.id.rb_pendente;
                break;
            case PREPARACAO:
                id = R.id.rb_preparacao;
                break;
            case SAIU_ENTREGA:
                id = R.id.rb_saiu_entrega;
                break;
            case CANCELADO_EMPRESA:
            case CANCELADO_USUARIO:
                id = R.id.rb_cancelado;
                break;
            default:
                id = R.id.rb_pendente;
                break;
        }

        rgStatus.check(id);

        if (pedido.getStatusPedido() == StatusPedido.ENTREGUE ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_USUARIO ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_EMPRESA) {
            rbPendente.setEnabled(false);
            rbPreparacao.setEnabled(false);
            rbSaiuEntrega.setEnabled(false);
            rbEntregue.setEnabled(false);
            rbCancelado.setEnabled(false);
        }
    }

    private void addPedidoList(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusPedido.CANCELADO_EMPRESA &&
        pedido.getStatusPedido() != StatusPedido.CANCELADO_USUARIO &&
        pedido.getStatusPedido() != StatusPedido.ENTREGUE) {
            pedidoList.add(pedido);
        }
    }

    private void configRv() {
        rvPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPedidos.setHasFixedSize(true);
        empresaPedidoAdapter = new EmpresaPedidoAdapter(pedidoList, getContext(), this);
        rvPedidos.setAdapter(empresaPedidoAdapter);
    }

    private void iniciarComponentes(View view) {
        rvPedidos = view.findViewById(R.id.rv_pedidos);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
    }

    @Override
    public void onClick(Pedido pedido, int rota) {
        if (rota == 0) {
            showDialogStatus(pedido);
        } else if (rota == 1) {
            Intent intent = new Intent(getActivity(), PedidoDetalheActivity.class);
            intent.putExtra("pedidoSelecionado", pedido);
            intent.putExtra("acesso", "empresa");
            startActivity(intent);
        }
    }
}
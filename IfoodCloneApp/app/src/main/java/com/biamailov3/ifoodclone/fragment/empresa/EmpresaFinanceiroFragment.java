package com.biamailov3.ifoodclone.fragment.empresa;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.FinanceiroAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmpresaFinanceiroFragment extends Fragment {

    private FinanceiroAdapter financeiroAdapter;
    private List<Pedido> pedidoList = new ArrayList<>();

    private EditText edtInicio;
    private EditText edtFinal;
    private Button btnFiltrar;
    private Button btnTodos;
    private ProgressBar progressBar;
    private TextView textReceita;
    private TextView textSaldo;
    private RecyclerView rvMovimentos;

    private String dataInicio;
    private String dataFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_financeiro, container, false);

        iniciarComponentes(view);
        recuperarTodosPedidos();
        configRv();
        configCliques();

        return view;
    }

    private void configRv() {
        rvMovimentos.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvMovimentos.setHasFixedSize(true);
        financeiroAdapter = new FinanceiroAdapter(pedidoList, requireContext());
        rvMovimentos.setAdapter(financeiroAdapter);
    }

    private void configCliques() {

        btnTodos.setOnClickListener(view -> recuperarTodosPedidos());

        btnFiltrar.setOnClickListener(view -> {
            dataInicio = edtInicio.getText().toString();
            dataFinal = edtFinal.getText().toString();

            if (!dataInicio.isEmpty()) {
                if (!dataFinal.isEmpty()) {

                    progressBar.setVisibility(View.VISIBLE);
                    filtrarPedidosData();

                } else {
                    edtFinal.requestFocus();
                    edtFinal.setError("Informe a data final.");
                }
            } else {
                edtInicio.requestFocus();
                edtInicio.setError("Informe a data de início.");
            }
        });
    }

    private void recuperarTodosPedidos() {
        DatabaseReference pedidosRef = FirebaseHelper.getDatabaseReference()
                .child("empresaPedidos")
                .child(FirebaseHelper.getIdFirebase());
        pedidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pedidoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pedido pedido = ds.getValue(Pedido.class);

                        pedidoList.add(pedido);
                    }

                    Collections.reverse(pedidoList);
                    financeiroAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    configValores();
                    ocultarTeclado();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filtrarPedidosData() {
        DatabaseReference pedidosRef = FirebaseHelper.getDatabaseReference()
                .child("empresaPedidos")
                .child(FirebaseHelper.getIdFirebase());
        pedidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pedidoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pedido pedido = ds.getValue(Pedido.class);

                        verificarDataPedidos(pedido);
                    }

                    Collections.reverse(pedidoList);
                    financeiroAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificarDataPedidos(Pedido pedido) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("PT", "br"));

        String strDataPedido = GetMask.getDate(pedido.getDataPedido(), 1);

        /*
            RESULTADO do compareTo()
            0 -> Datas iguais
            -1 -> Quando a data passada como parametro for maior que a data que invocou o metodo
            1 -> Quando a data passada como parametro for menor que a data que invocou o metodo
         */

        try {
            Date dataPedido = sdf.parse(strDataPedido);
            Date dataInicioConvertida = sdf.parse(dataInicio);
            Date dataFinalConvertida = sdf.parse(dataFinal);

            if ((dataInicioConvertida.compareTo(dataPedido) == 0 || dataInicioConvertida.compareTo(dataPedido) < 0)
                    && (dataFinalConvertida.compareTo(dataPedido) == 0) || dataFinalConvertida.compareTo(dataPedido) > 0) {
                pedidoList.add(pedido);
            }

        } catch (Exception e) {

        }

        if (pedidoList.size() > 0) {
            ocultarTeclado();
        }

        // atualizar valores de receita e saldo
        configValores();

    }

    // no curso o app nao vai mexer com as despesas
    private void configValores() {
        double saldo = 0.0;
        for (Pedido pedido : pedidoList) {
            saldo += pedido.getTotalPedido() + pedido.getTaxaEntrega();
        }

        textReceita.setText(getString(R.string.text_valor, GetMask.getValor(saldo)));
        textSaldo.setText(getString(R.string.text_valor, GetMask.getValor(saldo)));
    }

    private void iniciarComponentes(View view) {
        edtInicio = view.findViewById(R.id.edt_inicio);
        edtFinal = view.findViewById(R.id.edt_final);
        btnFiltrar = view.findViewById(R.id.btn_filtrar);
        btnTodos = view.findViewById(R.id.btn_todos);
        progressBar = view.findViewById(R.id.progressBar);
        textReceita = view.findViewById(R.id.text_receita);
        textSaldo = view.findViewById(R.id.text_saldo);
        rvMovimentos = view.findViewById(R.id.rv_movimentos);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(btnFiltrar.getWindowToken(), 0);
    }
}
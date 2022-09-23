package com.biamailov3.ifoodclone.fragment.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;

public class EmpresaFinanceiroFragment extends Fragment {

    private EditText edtInicio;
    private EditText edtFinal;
    private Button btnFiltrar;
    private Button btnTodos;
    private ProgressBar progressBar;
    private TextView textReceita;
    private TextView textSaldo;
    private RecyclerView rvDespesas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa_financeiro, container, false);

        iniciarComponentes(view);

        return view;
    }

    private void iniciarComponentes(View view) {
        edtInicio = view.findViewById(R.id.edt_inicio);
        edtFinal = view.findViewById(R.id.edt_final);
        btnFiltrar = view.findViewById(R.id.btn_filtrar);
        btnTodos = view.findViewById(R.id.btn_todos);
        progressBar = view.findViewById(R.id.progressBar);
        textReceita = view.findViewById(R.id.text_receita);
        textSaldo = view.findViewById(R.id.text_saldo);
        rvDespesas = view.findViewById(R.id.rv_despesas);
    }
}
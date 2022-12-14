package com.br.ecommerce.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.ecommerce.DAO.ItemDAO;
import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.activity.loja.LojaFormProdutoActivity;
import com.br.ecommerce.adapter.CarrinhoAdapter;
import com.br.ecommerce.databinding.DialogLojaProdutoBinding;
import com.br.ecommerce.databinding.DialogRemoverCarrinhoBinding;
import com.br.ecommerce.databinding.FragmentUsuarioCarrinhoBinding;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioCarrinhoFragment extends Fragment implements CarrinhoAdapter.OnClick {

    private FragmentUsuarioCarrinhoBinding binding;

    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private ItemPedidoDAO itemPedidoDAO;
    private ItemDAO itemDAO;

    private CarrinhoAdapter carrinhoAdapter;

    private AlertDialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioCarrinhoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemDAO = new ItemDAO(requireContext());
        itemPedidoDAO = new ItemPedidoDAO(requireContext());
        itemPedidoList.addAll(itemPedidoDAO.getList());

        configRv();
    }

    private void configRv() {
        Collections.reverse(itemPedidoList);
        binding.rvProdutos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProdutos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoList, itemPedidoDAO, requireContext(), this);
        binding.rvProdutos.setAdapter(carrinhoAdapter);

        configTotalCarrinho();
    }

    private void configTotalCarrinho() {
        binding.textValor.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalCarrinho())));
    }

    private void configQtdProduto(int position, String operacao) {
        ItemPedido itemPedido = itemPedidoList.get(position);

        if (operacao.equals("mais")) {
            itemPedido.setQuantidade(itemPedido.getQuantidade() + 1);
            itemPedidoDAO.atualizar(itemPedido);

            itemPedidoList.set(position, itemPedido);
        } else {

            if (itemPedido.getQuantidade() > 1) {
                itemPedido.setQuantidade(itemPedido.getQuantidade() - 1);
                itemPedidoDAO.atualizar(itemPedido);
                itemPedidoList.set(position, itemPedido);
            }

        }

        carrinhoAdapter.notifyDataSetChanged();
        configTotalCarrinho();
    }

    private void showDialogRemover(Produto produto) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);

        DialogRemoverCarrinhoBinding dialogBinding = DialogRemoverCarrinhoBinding.inflate(LayoutInflater.from(requireContext()));

        Picasso.get().load(produto.getUrlsImagens().get(0).getCaminhoImagem())
                .into(dialogBinding.imagemProduto);

        dialogBinding.txtNomeProduto.setText(produto.getTitulo());

        dialogBinding.btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnAddFavorito.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialogBinding.btnRemover.setOnClickListener(view -> {
            produto.remover();
            dialog.dismiss();
            Toast.makeText(requireContext(), "O produto foi removido.", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });



        builder.setView(dialogBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onClickListener(int position, String operacao) {

        int idProduto = itemPedidoList.get(position).getId();
        Produto produto = itemPedidoDAO.getProduto(idProduto);

        switch (operacao) {
            case "detalhe":
                break;
            case "remover":
                showDialogRemover(produto);
                break;
            case "menos":
            case "mais":
                configQtdProduto(position, operacao);
                break;
        }

    }
}
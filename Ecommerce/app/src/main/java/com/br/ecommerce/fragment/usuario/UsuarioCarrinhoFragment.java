package com.br.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.ecommerce.DAO.ItemDAO;
import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.adapter.CarrinhoAdapter;
import com.br.ecommerce.databinding.FragmentUsuarioCarrinhoBinding;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioCarrinhoFragment extends Fragment implements CarrinhoAdapter.OnClick {

    private FragmentUsuarioCarrinhoBinding binding;

    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private ItemPedidoDAO itemPedidoDAO;
    private ItemDAO itemDAO;

    private CarrinhoAdapter carrinhoAdapter;

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

        configSaldoCarrinho();
    }

    private void configSaldoCarrinho() {
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
        configSaldoCarrinho();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onClickListener(int position, String operacao) {

        switch (operacao) {
            case "detalhe":
                break;
            case "remover":
                break;
            case "menos":
            case "mais":
                configQtdProduto(position, operacao);
                break;
        }

    }
}
package com.br.ecommerce.fragment.usuario;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.br.ecommerce.activity.usuario.UsuarioResumoPedidoActivity;
import com.br.ecommerce.activity.usuario.UsuarioSelecionaEnderecoActivity;
import com.br.ecommerce.activity.usuario.UsuarioSelecionaPagamentoActivity;
import com.br.ecommerce.adapter.CarrinhoAdapter;
import com.br.ecommerce.autenticacao.LoginActivity;
import com.br.ecommerce.databinding.DialogRemoverCarrinhoBinding;
import com.br.ecommerce.databinding.FragmentUsuarioCarrinhoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Favorito;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioCarrinhoFragment extends Fragment implements CarrinhoAdapter.OnClick {

    private FragmentUsuarioCarrinhoBinding binding;

    private final List<ItemPedido> itemPedidoList = new ArrayList<>();
    private final List<String> idsFavoritos = new ArrayList<>();
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
        recuperaFavoritos();
        configCliques();
    }

    @Override
    public void onStart() {
        super.onStart();

        configInfo();
    }
    private void configCliques() {
        binding.btnContinuar.setOnClickListener(view -> {
            if (FirebaseHelper.getAutenticado()){
                startActivity(new Intent(requireContext(), UsuarioSelecionaPagamentoActivity.class));
            } else {
                resultLauncher.launch(new Intent(requireContext(), LoginActivity.class));
            }
        });
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
        binding.textValor.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalPedido())));
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

    private void showDialogRemover(Produto produto, int position) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);

        DialogRemoverCarrinhoBinding dialogBinding = DialogRemoverCarrinhoBinding.inflate(LayoutInflater.from(requireContext()));

        if (idsFavoritos.contains(produto.getId())) {
            dialogBinding.likeButton.setLiked(true);
        }

        dialogBinding.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (FirebaseHelper.getAutenticado()) {
                    salvarFavorito(produto);
                } else {
                    Toast.makeText(requireContext(), "Você não está autenticado no app.", Toast.LENGTH_SHORT).show();
                    dialogBinding.likeButton.setLiked(false);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                salvarFavorito(produto);
            }
        });

        Picasso.get().load(produto.getUrlsImagens().get(0).getCaminhoImagem())
                .into(dialogBinding.imagemProduto);

        dialogBinding.txtNomeProduto.setText(produto.getTitulo());

        dialogBinding.btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnAddFavorito.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialogBinding.btnRemover.setOnClickListener(view -> {
            removerProdutoCarrinho(position);
            dialog.dismiss();
            Toast.makeText(requireContext(), "O produto foi removido.", Toast.LENGTH_SHORT).show();
        });

        builder.setView(dialogBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }

    private void salvarFavorito(Produto produto) {
        if (!idsFavoritos.contains(produto.getId())) {
            idsFavoritos.add(produto.getId());
        } else {
            idsFavoritos.remove(produto.getId());
        }
        Favorito.salvar(idsFavoritos);
    }

    private void removerProdutoCarrinho(int position) {
        ItemPedido itemPedido = itemPedidoList.get(position);

        itemPedidoList.remove(itemPedido);
        itemPedidoDAO.remover(itemPedido);
        itemDAO.remover(itemPedido);
        carrinhoAdapter.notifyDataSetChanged();
        configInfo();
        configTotalCarrinho();
    }

    private void configInfo() {
        if (itemPedidoList.isEmpty()) {
            binding.textInfo.setVisibility(View.VISIBLE);
        } else {
            binding.textInfo.setVisibility(View.GONE);
        }
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    startActivity(new Intent(requireContext(), UsuarioSelecionaEnderecoActivity.class));
                }
            }
    );

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
                showDialogRemover(produto, position);
                break;
            case "menos":
            case "mais":
                configQtdProduto(position, operacao);
                break;
        }

    }
}
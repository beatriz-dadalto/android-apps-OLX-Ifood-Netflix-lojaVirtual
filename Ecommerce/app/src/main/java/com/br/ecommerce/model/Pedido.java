package com.br.ecommerce.model;

import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {

    private String id;
    private StatusPedido statusPedido;
    private String idCliente;
    private Endereco endereco;
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private long dataPedido;
    private long dataStatusPedido;
    private double total;
    private String pagamento;
    private double desconto;
    private double acrescimo;

    public Pedido() {
        DatabaseReference pedidoRef = FirebaseHelper.getDatabaseReference();
        this.setId(pedidoRef.push().getKey());
    }

    public void salvar(boolean novoPedido) {
        DatabaseReference usuarioPedidoRef = FirebaseHelper.getDatabaseReference()
                .child("usuarioPedidos")
                .child(this.getIdCliente())
                .child(this.getId());
        usuarioPedidoRef.setValue(this);

        DatabaseReference lojaPedidoRef = FirebaseHelper.getDatabaseReference()
                .child("lojaPedidos")
                .child(this.getId());
        lojaPedidoRef.setValue(this);

        if (novoPedido) {
            DatabaseReference dataPedidoUsuarioRef = usuarioPedidoRef
                    .child("dataPedido");
            dataPedidoUsuarioRef.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataPedidoLojaRef = lojaPedidoRef
                    .child("dataPedido");
            dataPedidoLojaRef.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataStatusPedidoUsuarioRef = usuarioPedidoRef
                    .child("dataStatusPedido");
            dataStatusPedidoUsuarioRef.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataStatusPedidoLojaRef = lojaPedidoRef
                    .child("dataStatusPedido");
            dataStatusPedidoLojaRef.setValue(ServerValue.TIMESTAMP);
        } else { // Editando pedido
            DatabaseReference dataStatusPedidoUsuarioRef = usuarioPedidoRef
                    .child("dataStatusPedido");
            dataStatusPedidoUsuarioRef.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataStatusPedidoLojaRef = lojaPedidoRef
                    .child("dataStatusPedido");
            dataStatusPedidoLojaRef.setValue(ServerValue.TIMESTAMP);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<ItemPedido> getItemPedidoList() {
        return itemPedidoList;
    }

    public void setItemPedidoList(List<ItemPedido> itemPedidoList) {
        this.itemPedidoList = itemPedidoList;
    }

    public long getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(long dataPedido) {
        this.dataPedido = dataPedido;
    }

    public long getDataStatusPedido() {
        return dataStatusPedido;
    }

    public void setDataStatusPedido(long dataStatusPedido) {
        this.dataStatusPedido = dataStatusPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(double acrescimo) {
        this.acrescimo = acrescimo;
    }
}
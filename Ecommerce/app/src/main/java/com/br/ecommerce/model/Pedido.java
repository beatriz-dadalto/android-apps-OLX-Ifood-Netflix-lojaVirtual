package com.br.ecommerce.model;

import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {

    private String id;
    private PedidoStatus status;
    private String idCliente;
    private Endereco endereco;
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private long data;
    private double total;
    private String pagamento;

    public Pedido() {
        DatabaseReference pedidoRef = FirebaseHelper.getDatabaseReference();
        setId(pedidoRef.push().getKey());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
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
}

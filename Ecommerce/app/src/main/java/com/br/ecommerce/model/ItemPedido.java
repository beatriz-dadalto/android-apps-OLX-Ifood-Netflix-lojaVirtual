package com.br.ecommerce.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ItemPedido implements Serializable {

    private int id;
    private String idProduto;
    private String nomeProduto;
    private String urlImagemProduto;
    private double valor;
    private int quantidade;

    public ItemPedido() {
    }

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getUrlImagemProduto() {
        return urlImagemProduto;
    }

    public void setUrlImagemProduto(String urlImagemProduto) {
        this.urlImagemProduto = urlImagemProduto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

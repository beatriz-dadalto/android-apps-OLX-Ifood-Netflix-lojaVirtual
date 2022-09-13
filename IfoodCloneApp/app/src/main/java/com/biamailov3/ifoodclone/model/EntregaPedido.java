package com.biamailov3.ifoodclone.model;

public class EntregaPedido {

    private String formaPagamento;
    private Endereco endereco;

    public EntregaPedido() {
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}

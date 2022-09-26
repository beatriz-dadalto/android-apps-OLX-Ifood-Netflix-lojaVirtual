package com.br.bancodigital.model;

import java.io.Serializable;

public class Deposito implements Serializable {

    private String id;
    private long data;
    private Double valor;

    public Deposito() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}

package com.br.bancodigital.model;

import java.io.Serializable;

public class Deposito implements Serializable {

    private String id;
    private long data;
    private double valor;

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

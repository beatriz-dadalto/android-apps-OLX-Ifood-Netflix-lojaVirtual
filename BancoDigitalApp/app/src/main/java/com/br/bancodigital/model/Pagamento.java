package com.br.bancodigital.model;

import java.io.Serializable;

public class Pagamento implements Serializable {

    private String id;
    private String idCobranca;
    private String idUserOrigem;
    private String idUserDestino;
    private double valor;
    private long data;

    public Pagamento() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCobranca() {
        return idCobranca;
    }

    public void setIdCobranca(String idCobranca) {
        this.idCobranca = idCobranca;
    }

    public String getIdUserOrigem() {
        return idUserOrigem;
    }

    public void setIdUserOrigem(String idUserOrigem) {
        this.idUserOrigem = idUserOrigem;
    }

    public String getIdUserDestino() {
        return idUserDestino;
    }

    public void setIdUserDestino(String idUserDestino) {
        this.idUserDestino = idUserDestino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}

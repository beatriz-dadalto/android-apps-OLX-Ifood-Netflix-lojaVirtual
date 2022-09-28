package com.br.bancodigital.model;

import com.br.bancodigital.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Transferencia implements Serializable {

    private String id;
    private String idUserOrigem;
    private String idUserDestino;
    private long data;
    private double valor;

    public Transferencia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

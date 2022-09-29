package com.br.bancodigital.model;

import com.br.bancodigital.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Cobranca implements Serializable {

    private String id;
    private String idEmitente;
    private String idDestinatario;
    private double valor;
    private long data;
    private boolean paga = false;

    public Cobranca() {
        // gerar um id quando for instanciada
        DatabaseReference cobrancaRef = FirebaseHelper.getDatabaseReference();
        setId(cobrancaRef.push().getKey());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmitente() {
        return idEmitente;
    }

    public void setIdEmitente(String idEmitente) {
        this.idEmitente = idEmitente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
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

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }
}

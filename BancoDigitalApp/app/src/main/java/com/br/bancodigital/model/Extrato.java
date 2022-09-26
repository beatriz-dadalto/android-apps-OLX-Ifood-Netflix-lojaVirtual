package com.br.bancodigital.model;

import com.br.bancodigital.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Extrato implements Serializable {

    private String id;
    private String operacao;
    private Long data;
    private Double valor;
    private String tipo;

    public Extrato() {
        // sempre que instanciar vai criar um id unico para o extrato
        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference();
        setId(extratoRef.push().getKey());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

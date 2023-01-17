package com.br.ecommerce.model;

import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class FormaPagamento implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private Double valor;
    private TipoValor tipoValor; // "DESCONTO" ou "ACRESCIMO"

    public FormaPagamento() {
        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference();
        // id toda vez que for instanciada
        this.setId(pagamentoRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
                .child("formapagamento")
                .child(this.getId());
        pagamentoRef.setValue(this);
    }

    public void remover() {
        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
                .child("formapagamento")
                .child(this.getId());
        pagamentoRef.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoValor getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(TipoValor tipoValor) {
        this.tipoValor = tipoValor;
    }
}

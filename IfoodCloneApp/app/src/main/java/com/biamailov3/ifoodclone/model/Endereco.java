package com.biamailov3.ifoodclone.model;

import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Endereco implements Serializable {

    private String id;
    private String logradouro;
    private String bairro;
    private String municipio;
    private String referencia;

    public Endereco() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference();
        setId(enderecoRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        enderecoRef.setValue(this);
    }

    public void remover() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        enderecoRef.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}

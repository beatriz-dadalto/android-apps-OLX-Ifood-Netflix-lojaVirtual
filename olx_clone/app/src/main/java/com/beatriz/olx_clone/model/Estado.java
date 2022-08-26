package com.beatriz.olx_clone.model;

import java.io.Serializable;

public class Estado implements Serializable {

    private String uf;
    private String regiao;
    private String nome;
    private String ddd;

    public Estado() {
    }

    public Estado(String nome, String uf) {
        this.nome = nome;
        this.uf = uf;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}

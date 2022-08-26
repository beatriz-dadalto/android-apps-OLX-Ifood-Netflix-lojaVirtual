package com.beatriz.casaportemporada.model;

import java.io.Serializable;

public class Filtro implements Serializable {

    private int quantidadeQuarto;
    private int quantidadeBanheiro;
    private int quantidadeGaragem;

    public int getQuantidadeQuarto() {
        return quantidadeQuarto;
    }

    public void setQuantidadeQuarto(int quantidadeQuarto) {
        this.quantidadeQuarto = quantidadeQuarto;
    }

    public int getQuantidadeBanheiro() {
        return quantidadeBanheiro;
    }

    public void setQuantidadeBanheiro(int quantidadeBanheiro) {
        this.quantidadeBanheiro = quantidadeBanheiro;
    }

    public int getQuantidadeGaragem() {
        return quantidadeGaragem;
    }

    public void setQuantidadeGaragem(int quantidadeGaragem) {
        this.quantidadeGaragem = quantidadeGaragem;
    }
}

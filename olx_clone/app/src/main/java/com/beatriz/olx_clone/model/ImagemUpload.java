package com.beatriz.olx_clone.model;

import java.io.Serializable;

public class ImagemUpload implements Serializable {

    private String caminhoImagem;
    private int index;

    public ImagemUpload() {
    }

    public ImagemUpload(String caminhoImagem, int index) {
        this.caminhoImagem = caminhoImagem;
        this.index = index;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

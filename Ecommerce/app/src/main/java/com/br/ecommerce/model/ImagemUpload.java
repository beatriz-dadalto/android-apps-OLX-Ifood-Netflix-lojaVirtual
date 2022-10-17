package com.br.ecommerce.model;

import java.io.Serializable;

public class ImagemUpload implements Serializable {

    private int index;
    private String caminhoImagem;

    public ImagemUpload() {
    }

    public ImagemUpload(int index, String caminhoImagem) {
        this.index = index;
        this.caminhoImagem = caminhoImagem;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
}

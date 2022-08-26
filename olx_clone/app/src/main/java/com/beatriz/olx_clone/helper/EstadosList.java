package com.beatriz.olx_clone.helper;

import com.beatriz.olx_clone.model.Estado;

import java.util.ArrayList;
import java.util.List;

public class EstadosList {

    public static List<Estado> getList() {
        List<Estado> estadoList = new ArrayList<>();

        estadoList.add(new Estado("Brasil", ""));
        estadoList.add(new Estado("Acre", "AC"));
        estadoList.add(new Estado("Alagoas", "AL"));
        estadoList.add(new Estado("Amapá", "AP"));
        estadoList.add(new Estado("Amazonas", "AM"));
        estadoList.add(new Estado("Bahia", "BA"));
        estadoList.add(new Estado("Ceará", "CE"));
        estadoList.add(new Estado("Distrito Federal", "DF"));
        estadoList.add(new Estado("Espírito Santo", "ES"));
        estadoList.add(new Estado("Goiás", "GO"));
        estadoList.add(new Estado("Maranhão", "MA"));
        estadoList.add(new Estado("Mato Grosso", "MT"));
        estadoList.add(new Estado("Mato Grosso do Sul", "MS"));
        estadoList.add(new Estado("Minas Gerais", "MG"));
        estadoList.add(new Estado("Pará", "PA"));
        estadoList.add(new Estado("Paraíba", "PB"));
        estadoList.add(new Estado("Paraná", "PR"));
        estadoList.add(new Estado("Pernambuco", "PE"));
        estadoList.add(new Estado("Piauí", "PI"));
        estadoList.add(new Estado("Rio de Janeiro", "RJ"));
        estadoList.add(new Estado("Rio Grande do Norte", "RN"));
        estadoList.add(new Estado("Rio Grande do Sul", "RS"));
        estadoList.add(new Estado("Rondônia", "RO"));
        estadoList.add(new Estado("Roraima", "RR"));
        estadoList.add(new Estado("Santa Catarina", "SC"));
        estadoList.add(new Estado("São Paulo", "SP"));
        estadoList.add(new Estado("Sergipe", "SE"));
        estadoList.add(new Estado("Tocantins", "TO"));

        return estadoList;
    }
}

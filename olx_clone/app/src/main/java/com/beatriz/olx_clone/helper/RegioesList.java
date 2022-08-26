package com.beatriz.olx_clone.helper;

import java.util.ArrayList;
import java.util.List;

public class RegioesList {

    public static List<String> getList(String ufEstado) {
        List<String> regioes = new ArrayList<>();

        regioes.add("Todas as regiões");

        switch (ufEstado) {
            case "AC":
                regioes.add("DDD 68 - Acre");
                break;
            case "AL":
                regioes.add("DDD 82 - Alagoas");
                break;
            case "AP":
                regioes.add("DDD 96 - Amapá");
                break;
            case "AM":
                regioes.add("DDD 92 - Amazonas");
                regioes.add("DDD 97 - Leste do Amazonas");
                break;
            case "BA":
                regioes.add("DDD 71 - Salvador");
                regioes.add("DDD 73 - Sul da Bahia");
                regioes.add("DDD 74 - Juazeiro, Jacobina e região");
                regioes.add("DDD 75 - F. de Santana, Alagoinhas e região");
                regioes.add("DDD 77 - V da Conquista, Barreiras e região");
                break;
            case "CE":
                regioes.add("DDD 85 - Fortaleza e região");
                regioes.add("DDD 88 - Juazeiro do Norte, Sobral e região");
                break;
            case "DF":
                regioes.add("DDD 61 - Distrito Federal e região");
                break;
            case "ES":
                regioes.add("DDD 27 - Norte do Espírito Santo");
                regioes.add("DDD 28 - Sul do Espírito Santo");
                break;
            case "GO":
                regioes.add("DDD 62 - Grande Goiânia e Anápolis");
                regioes.add("DDD 64 - Rio Verde, Caldas Novas e região");
                break;
            case "MA":
                regioes.add("DDD 98 - Região de São Luíz");
                regioes.add("DDD 99 - Imperatriz, Caxias e região");
                break;
            case "MT":
                regioes.add("DDD 65 - Cuiabá e região");
                regioes.add("DDD 66 - Rondonópolis, Sinop e região");
                break;
            case "MS":
                regioes.add("DDD 67 - Mato Grosso do Sul");
                break;
            case "MG":
                regioes.add("DDD 31 - Belo Horizonte e região");
                regioes.add("DDD 32 - Juiz de Fora e região");
                regioes.add("DDD 33 - Gov. Valadares, T. Otoni e região");
                regioes.add("DDD 34 - Uberlândia, Uberaba e região");
                regioes.add("DDD 35 - Poços de Caldas e Varginha");
                regioes.add("DDD 37 - Divinópolis e região");
                regioes.add("DDD 38 - Mtes Claros, Diamantina e região");
                break;
            case "PA":
                regioes.add("DDD 91 - Região de Belém");
                regioes.add("DDD 93 - Região de Santarém");
                regioes.add("DDD 94 - Região de Marabá");
                break;
            case "PB":
                regioes.add("DDD 83 - Paraíba");
                break;
            case "PR":
                regioes.add("DDD 41 - Curitiba e região");
                regioes.add("DDD 42 - Pta Grossa, Guarapuava e região");
                regioes.add("DDD 43 - Londrina e região");
                regioes.add("DDD 44 - Maringá e região");
                regioes.add("DDD 45 - Foz do Iguaçu, Cascavel e região");
                regioes.add("DDD 46 - F. Beltrão e Pato Branco e região");
                break;
            case "PE":
                regioes.add("DDD 81 - Grande Recife");
                regioes.add("DDD 87 - Petrolina, Garanhuns e região");
                break;
            case "PI":
                regioes.add("DDD 86 - Teresina, Parnaíba e região");
                regioes.add("DDD 89 - Picos, Floriano e região");
                break;
            case "RJ":
                regioes.add("DDD 21 - Rio de Janeiro e região");
                regioes.add("DDD 22 - Norte do Estado e Região dos Lagos");
                regioes.add("DDD 24 - Serra, Angra dos Reis e região");
                break;
            case "RN":
                regioes.add("DDD 84 - Rio Grande do Norte");
                break;
            case "RS":
                regioes.add("DDD 51 - Porto Alegre e região");
                regioes.add("DDD 53 - Pelotas, Bagé, Rio Gde e região");
                regioes.add("DDD 54 - Caxias do Sul e região");
                regioes.add("DDD 55 - Sta Maria, Cruz Alta e região");
                break;
            case "RO":
                regioes.add("DDD 69 - Rondônia");
                break;
            case "RR":
                regioes.add("DDD 96 - Roraima");
                break;
            case "SC":
                regioes.add("DDD 47 - Norte de Santa Catarina");
                regioes.add("DDD 48 - Florianópolis e região");
                regioes.add("DDD 49 - Oeste de Santa Catarina");
                break;
            case "SP":
                regioes.add("DDD 11 - São Paulo e região");
                regioes.add("DDD 12 - V. do Paraíba e Litoral Norte");
                regioes.add("DDD 13 - Baixada Santista e Litoral Sul");
                regioes.add("DDD 14 - Bauru, Marília e região");
                regioes.add("DDD 15 - Sorocaba e região");
                regioes.add("DDD 16 - Ribeirão Preto e região");
                regioes.add("DDD 17 - S. José do Rio Preto e região");
                regioes.add("DDD 18 - Presidente Prudente e região");
                regioes.add("DDD 19 - Grande Campinas");
                break;
            case "SE":
                regioes.add("DDD 79 - Sergipe");
                break;
            case "TO":
                regioes.add("DDD 63 - Tocantins");
                break;
        }

        return regioes;
    }
}

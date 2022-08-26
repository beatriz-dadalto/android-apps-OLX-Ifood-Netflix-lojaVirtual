package com.beatriz.olx_clone.helper;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaList {

    public static List<Categoria> getList(boolean todas) {
        List<Categoria> categoriaList = new ArrayList<>();

        // mostrar a opcao 'Todas as categorias' apenas quando o user escolher filtro
        if (todas) {
            categoriaList.add(new Categoria(R.drawable.ic_todas_as_categorias, "Todas as categorias"));
        }
        categoriaList.add(new Categoria(R.drawable.ic_autos_e_pecas, "Autos e peças"));
        categoriaList.add(new Categoria(R.drawable.ic_imoveis, "Imóveis"));
        categoriaList.add(new Categoria(R.drawable.ic_eletronico_e_celulares, "Eletrônicos e Celulares"));
        categoriaList.add(new Categoria(R.drawable.ic_para_a_sua_casa, "para sua casa"));
        categoriaList.add(new Categoria(R.drawable.ic_moda_e_beleza, "Moda e Beleza"));
        categoriaList.add(new Categoria(R.drawable.ic_esporte_e_lazer, "Esporte e Lazer"));
        categoriaList.add(new Categoria(R.drawable.ic_musica_e_hobbies, "Músicas e Hobbies"));
        categoriaList.add(new Categoria(R.drawable.ic_artigos_infantis, "Artigos infantis"));
        categoriaList.add(new Categoria(R.drawable.ic_animais_de_estimacao, "Animais de Estimação"));
        categoriaList.add(new Categoria(R.drawable.ic_agro_e_industria, "Agro e Indústria"));
        categoriaList.add(new Categoria(R.drawable.ic_comercio_e_escritorio, "Comércio e Escritório"));
        categoriaList.add(new Categoria(R.drawable.ic_servicos, "Serviços"));
        categoriaList.add(new Categoria(R.drawable.ic_vagas_de_emprego, "Vagas de Emprego"));

        return categoriaList;
    }
}

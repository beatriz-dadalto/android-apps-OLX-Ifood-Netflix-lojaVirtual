package com.beatriz.olx_clone.helper;

import android.app.Activity;
import android.content.SharedPreferences;

import com.beatriz.olx_clone.model.Estado;
import com.beatriz.olx_clone.model.Filtro;

// SP = Shared Preference
public class SPFiltro {

    // nome do arquivo que sera criado
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    public static void setFiltro(Activity activity, String chave, String valor) {
        // mode zero apenas o dispositivo pode acessar
        SharedPreferences preferences = activity.getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(chave, valor);
        editor.commit();
    }

    public static Filtro getFiltro(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        // recuperar as informacoes do filtro
        String pesquisa = preferences.getString("pesquisa", "");
        String ufEstado = preferences.getString("ufEstado", "");
        String categoria = preferences.getString("categoria", "");
        String nomeEstado = preferences.getString("nomeEstado", "");
        String regiao = preferences.getString("regiao", "");
        String ddd = preferences.getString("ddd", "");

        String valorMin = preferences.getString("valorMin", "");
        String valorMax = preferences.getString("valorMax", "");

        Estado estado = new Estado();
        estado.setUf(ufEstado);
        estado.setNome(nomeEstado);
        estado.setDdd(ddd);
        estado.setRegiao(regiao);

        Filtro filtro = new Filtro();
        filtro.setEstado(estado);
        filtro.setPesquisa(pesquisa);
        filtro.setCategoria(categoria);

        if (!valorMin.isEmpty()) {
            filtro.setValorMin(Integer.parseInt(valorMin));
        }
        if (!valorMax.isEmpty()) {
            filtro.setValorMax(Integer.parseInt(valorMax));
        }

        return filtro;
    }

    public static void limparFiltros(Activity activity) {
        setFiltro(activity, "regiao", "");
        setFiltro(activity, "ufEstado", "");
        setFiltro(activity, "categoria", "");
        setFiltro(activity, "nomeEstado", "");
        setFiltro(activity, "ddd", "");
    }
}

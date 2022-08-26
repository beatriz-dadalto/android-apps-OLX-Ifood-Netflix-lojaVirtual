package com.beatriz.olx_clone.api;


import com.beatriz.olx_clone.model.Local;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    // o tipo de info que quero recuperar do api https://viacep.com.br/
    @GET("{cep}/json/")
    Call<Local> recuperarCEP(@Path("cep") String cep);

}

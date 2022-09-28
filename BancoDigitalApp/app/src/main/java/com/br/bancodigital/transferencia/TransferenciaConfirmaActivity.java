package com.br.bancodigital.transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.bancodigital.R;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Transferencia;
import com.br.bancodigital.model.Usuario;
import com.squareup.picasso.Picasso;

public class TransferenciaConfirmaActivity extends AppCompatActivity {

    private TextView textValor;
    private TextView textUsuario;
    private ImageView imagemUsuario;

    private Usuario usuario;
    private Transferencia transferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia_confirma);

        configToolbar();
        iniciaComponentes();
        configDados();
    }

    private void configDados(){
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        transferencia = (Transferencia) getIntent().getSerializableExtra("transferencia");

        textUsuario.setText(usuario.getNome());
        textValor.setText(getString(R.string.text_valor, GetMask.getValor(transferencia.getValor())));
        if (usuario.getUrlImagem() != null) {
            Picasso.get().load(usuario.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .into(imagemUsuario);
        }
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Confirme os dados");
    }

    private void iniciaComponentes() {
        textValor = findViewById(R.id.textValor);
        textUsuario = findViewById(R.id.textUsuario);
        imagemUsuario = findViewById(R.id.imagemUsuario);
    }
}
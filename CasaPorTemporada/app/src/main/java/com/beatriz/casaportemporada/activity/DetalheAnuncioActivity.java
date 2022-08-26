package com.beatriz.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Anuncio;
import com.beatriz.casaportemporada.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetalheAnuncioActivity extends AppCompatActivity {

    private ImageView imgAnuncio;
    private TextView textTituloAnuncio;
    private TextView textDescricao;
    private EditText editQuarto;
    private EditText editBanheiro;
    private EditText editGaragem;

    private Anuncio anuncio;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_anuncio);

        iniciarComponentes();

        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncio");
        recuperarAnunciante();
        configDados();

        configCliques();

    }

    // traz info do anuncio via intent
    private void configDados() {
        if (anuncio != null) {
            Picasso.get().load(anuncio.getUrlImagem()).into(imgAnuncio);
            textTituloAnuncio.setText(anuncio.getTitulo());
            textDescricao.setText(anuncio.getDescricao());
            editQuarto.setText(anuncio.getQuarto());
            editBanheiro.setText(anuncio.getBanheiro());
            editGaragem.setText(anuncio.getGaragem());
        } else {
            Toast.makeText(this, "Não foi possível recuperar as informações", Toast.LENGTH_SHORT).show();
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        TextView textTituloToolbar = findViewById(R.id.text_titulo);
        textTituloToolbar.setText("Detalhe do Anúncio");

        imgAnuncio = findViewById(R.id.img_anuncio);
        textTituloAnuncio = findViewById(R.id.text_titulo_anuncio);
        textDescricao = findViewById(R.id.text_descricao);
        editQuarto = findViewById(R.id.edit_quarto);
        editBanheiro = findViewById(R.id.edit_banheiro);
        editGaragem = findViewById(R.id.edit_garagem);
    }

    public void ligar(View view) {
        if(usuario != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + usuario.getTelefone()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Carregando, aguarde!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperarAnunciante() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(anuncio.getIdUsuario());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
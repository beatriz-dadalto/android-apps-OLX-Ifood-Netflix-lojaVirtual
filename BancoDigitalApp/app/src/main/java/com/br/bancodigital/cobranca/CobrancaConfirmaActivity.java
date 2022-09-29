package com.br.bancodigital.cobranca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.bancodigital.R;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Cobranca;
import com.br.bancodigital.model.Notificacao;
import com.br.bancodigital.model.Transferencia;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CobrancaConfirmaActivity extends AppCompatActivity {

    private TextView textValor;
    private TextView textUsuario;
    private ImageView imagemUsuario;

    private Usuario usuarioDestino;
    private Usuario usuarioOrigem;
    private Cobranca cobranca;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobranca_confirma);

        configToolbar();
        iniciaComponentes();
        configDados();
        recuperaUsuarioOrigem();
    }

    public void confirmarCobranca(View view) {
        
    }

    private void recuperaUsuarioOrigem() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioOrigem = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void enviaNotificacao(String idOperacao) {
        Notificacao notificacao = new Notificacao();
        notificacao.setOperacao("TRANSFERENCIA");
        notificacao.setIdDestinatario(usuarioDestino.getId());
        notificacao.setIdEmitente(usuarioOrigem.getId());
        notificacao.setIdOperacao(idOperacao);
        notificacao.enviar();
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this, R.style.CustomAlertDialog
        );

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void configDados() {
        usuarioDestino = (Usuario) getIntent().getSerializableExtra("usuario");
        cobranca = (Cobranca) getIntent().getSerializableExtra("cobranca");

        textUsuario.setText(usuarioDestino.getNome());
        textValor.setText(getString(R.string.text_valor, GetMask.getValor(cobranca.getValor())));
        if (usuarioDestino.getUrlImagem() != null) {
            Picasso.get().load(usuarioDestino.getUrlImagem())
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
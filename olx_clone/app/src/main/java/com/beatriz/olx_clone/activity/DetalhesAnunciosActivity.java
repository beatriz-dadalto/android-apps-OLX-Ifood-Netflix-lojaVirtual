package com.beatriz.olx_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.adapter.SliderAdapter;
import com.beatriz.olx_clone.autenticacao.LoginActivity;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.helper.GetMask;
import com.beatriz.olx_clone.model.Anuncio;
import com.beatriz.olx_clone.model.Favorito;
import com.beatriz.olx_clone.model.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DetalhesAnunciosActivity extends AppCompatActivity {

    private SliderView sliderView;
    private TextView textTituloAnuncio;
    private TextView textValorAnuncio;
    private TextView textDataPublicacao;
    private TextView textDescricaoAnuncio;
    private TextView textCategoriaAnuncio;
    private TextView textCepAnuncio;
    private TextView textMunicipioAnuncio;
    private TextView textBairroAnuncio;
    private LikeButton likeButton;

    private Anuncio anuncio;
    private Usuario usuario;

    private final List<String> favoritosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_anuncios);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            anuncio = (Anuncio) bundle.getSerializable("anuncioSelecionado");
            configDados();
            recuperarUsuario();
        }

        configLikeButton();
        recuperarFavoritos();
        configCliques();
    }

    private void configLikeButton() {
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(FirebaseHelper.getAutenticado()) {
                    configSnackBar("", "Anúncio salvo", R.drawable.like_button_on_red, true);
                } else {
                    likeButton.setLiked(false);
                    alertAutenticacao("Para adicionar este anúncio a sua lista de favoritos é preciso estar autenticado no app. Deseja autenticar-se agora?");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                configSnackBar("DESFAZER", "Anúncio removido", R.drawable.like_button_off, false);
            }
        });
    }

    private void configSnackBar(String actionMsg, String msg, int icon, Boolean like) {
        configFavoritos(like);
        Snackbar snackbar = Snackbar.make(likeButton, msg, Snackbar.LENGTH_SHORT);
        snackbar.setAction(actionMsg, v -> {
            if (!like) {
                configFavoritos(true);
            }
        });

        TextView textSnackBar = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textSnackBar.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        textSnackBar.setCompoundDrawablePadding(24);
        snackbar.setActionTextColor(Color.parseColor("#F78323"))
                .setTextColor(Color.parseColor("#FFFFFF"))
                .show();
    }

    private void recuperarFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoritosList.add(ds.getValue(String.class));
                    }

                    if (favoritosList.contains(anuncio.getId())) {
                        likeButton.setLiked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configFavoritos(Boolean like) {
        if (like) {
            likeButton.setLiked(true);
            favoritosList.add(anuncio.getId());
        } else {
            likeButton.setLiked(false);
            favoritosList.remove(anuncio.getId());
        }

        Favorito favorito = new Favorito();
        favorito.setFavoritos(favoritosList);
        favorito.salvar();
    }

    private void alertAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Você não está autenticado");
        builder.setMessage(msg);
        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.dismiss();
        }).setPositiveButton("Sim", (dialog, which) -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_ligar).setOnClickListener(v -> ligarAnunciante());
    }

    private void ligarAnunciante() {
        if(FirebaseHelper.getAutenticado()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", usuario.getTelefone(), null));
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void recuperarUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(anuncio.getIdUsuario());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        sliderView.setSliderAdapter(new SliderAdapter(anuncio.getUrlImagens()));
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInSec(4);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        textTituloAnuncio.setText(anuncio.getTitulo());
        textValorAnuncio.setText(getString(R.string.valor_anuncio, GetMask.getValor(anuncio.getValor())));
        textDataPublicacao.setText(getString(R.string.data_publicacao, GetMask.getDate(anuncio.getDataPublicacao(), 3)));
        textDescricaoAnuncio.setText(anuncio.getDescricao());
        textCategoriaAnuncio.setText(anuncio.getCategoria());
        textCepAnuncio.setText(anuncio.getLocal().getCep());
        textMunicipioAnuncio.setText(anuncio.getLocal().getLocalidade());
        textBairroAnuncio.setText(anuncio.getLocal().getBairro());
    }

    private void iniciarComponentes() {
        sliderView = findViewById(R.id.sliderView);
        textTituloAnuncio = findViewById(R.id.text_titulo_anuncio);
        textValorAnuncio = findViewById(R.id.text_valor_anuncio);
        textDataPublicacao = findViewById(R.id.text_data_publicacao);
        textDescricaoAnuncio = findViewById(R.id.text_descricao_anuncio);
        textCategoriaAnuncio = findViewById(R.id.text_categoria_anuncio);
        textCepAnuncio = findViewById(R.id.text_cep_anuncio);
        textMunicipioAnuncio = findViewById(R.id.text_municipio_anuncio);
        textBairroAnuncio = findViewById(R.id.text_bairro_anuncio);
        likeButton = findViewById(R.id.like_button);
    }
}
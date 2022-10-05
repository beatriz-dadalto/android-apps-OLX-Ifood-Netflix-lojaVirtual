package com.br.netflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.br.netflix.R;
import com.br.netflix.adapter.AdapterCategoria;
import com.br.netflix.adapter.AdapterPost;
import com.br.netflix.helper.FirebaseHelper;
import com.br.netflix.model.Categoria;
import com.br.netflix.model.Download;
import com.br.netflix.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetalheActivity extends AppCompatActivity {

    private List<String> downloadList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private AdapterPost adapterPost;

    private TextView textTitulo;
    private ImageView imagemPost;
    private ImageView imageFake;
    private TextView textAno;
    private TextView textDuracao;
    private TextView textElenco;
    private ConstraintLayout btnAssistir;
    private ConstraintLayout btnBaixar;
    private TextView textSinopse;
    private RecyclerView rvPosts;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        iniciaComponentes();
        configRv();
        configDados();
        configCliques();
        recuperaPosts();
        recuperaDownloads();
    }

    private void configCliques() {
        findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());
        findViewById(R.id.btnBaixar).setOnClickListener(view -> efetuarDownloads());
    }

    private void efetuarDownloads() {
        if (!downloadList.contains(post.getId())) {
            downloadList.add(post.getId());
            Download.salvar(downloadList);

            Toast.makeText(this, "Download efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Você já fez o download anteriormente!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperaDownloads() {
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        downloadRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    downloadList.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaPosts() {
        DatabaseReference postsRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    postList.add(ds.getValue(Post.class));
                }

                adapterPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));
        rvPosts.setHasFixedSize(true);
        adapterPost = new AdapterPost(postList, this);
        rvPosts.setAdapter(adapterPost);
    }

    private void configDados() {
        post = (Post) getIntent().getSerializableExtra("postSelecionado");

        textTitulo.setText(post.getTitulo());
        Picasso.get().load(post.getImagem()).into(imagemPost);
        imageFake.setVisibility(View.GONE);
        textAno.setText(post.getAno());
        textDuracao.setText(getString(R.string.text_duracao, post.getDuracao()));
        textSinopse.setText(post.getSinopse());
        textElenco.setText(post.getElenco());
    }

    private void iniciaComponentes() {
        textTitulo = findViewById(R.id.textTitulo);
        imagemPost = findViewById(R.id.imagemPost);
        imageFake = findViewById(R.id.imageFake);
        textAno = findViewById(R.id.textAno);
        textDuracao = findViewById(R.id.textDuracao);
        textElenco = findViewById(R.id.textElenco);
        btnAssistir = findViewById(R.id.btnAssistir);
        btnBaixar = findViewById(R.id.btnBaixar);
        textSinopse = findViewById(R.id.textSinopse);
        rvPosts = findViewById(R.id.rvPosts);
    }
}
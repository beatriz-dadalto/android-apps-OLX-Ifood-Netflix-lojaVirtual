package com.br.ecommerce.activity.loja;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.databinding.ActivityLojaConfigBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Loja;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class LojaConfigActivity extends AppCompatActivity {

    private ActivityLojaConfigBinding binding;

    private String caminhoImagem = null;

    private Loja loja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperaLoja();
        iniciaComponentes();
        configCliques();
    }

    private void iniciaComponentes() {
        binding.edtPedidoMinimo.setLocale(new Locale("PT", "br"));
        binding.edtFrete.setLocale(new Locale("PT", "br"));
    }

    private void configCliques() {
        binding.include.textTitulo.setText("Configurações");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());

        binding.imgLogo.setOnClickListener(v -> {
            verificaPermissaoGaleria();
        });

        binding.btnSalvar.setOnClickListener(v -> {
            ocultaTeclado();
            if (loja != null) {
                validaDados();
            } else {
                Toast.makeText(this, "Recuperando os dados da empresa, aguarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recuperaLoja() {
        DatabaseReference lojaRef = FirebaseHelper.getDatabaseReference()
                .child("loja");
        lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loja = snapshot.getValue(Loja.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        if (loja.getUrlLogo() != null) {
            Glide
                    .with(this)
                    .load(loja.getUrlLogo())
                    .centerCrop()
                    .into(binding.imgLogo);
        }

        if (loja.getNome() != null) {
            binding.edtLoja.setText(loja.getNome());
        }

        if (loja.getCNPJ() != null) {
            binding.edtCNPJ.setText(loja.getCNPJ());
        }

        if (loja.getPedidoMinimo() != 0) {
            binding.edtPedidoMinimo.setText(String.valueOf(loja.getPedidoMinimo() * 10));
        }

        if (loja.getFreteGratis() != 0) {
            binding.edtFrete.setText(String.valueOf(loja.getFreteGratis() * 10));
        }

        if (loja.getPublicKey() != null) {
            binding.edtPublicKey.setText(loja.getPublicKey());
        }

        if (loja.getAccessToken() != null) {
            binding.edtAcessToken.setText(loja.getAccessToken());
        }

        if (loja.getParcelas() != 0) {
            binding.edtParcelas.setText(String.valueOf(loja.getParcelas()));
        }
    }

    private void validaDados() {
        String nomeLoja = binding.edtLoja.getText().toString().trim();
        String CNPJ = binding.edtCNPJ.getMasked();
        double pedidoMinimo = (double) binding.edtPedidoMinimo.getRawValue() / 100;
        double freteGratis = (double) binding.edtFrete.getRawValue() / 100;
        String publicKey = binding.edtPublicKey.getText().toString().trim();
        String accessToken = binding.edtAcessToken.getText().toString().trim();

        int parcelas = 0;
        String parcelasStr = binding.edtParcelas.getText().toString().trim();
        if (!parcelasStr.isEmpty()) {
            parcelas = Integer.parseInt(binding.edtParcelas.getText().toString().trim());
        }

        if (!publicKey.isEmpty()) {
            if (!accessToken.isEmpty()) {
                if (parcelas > 0 && parcelas <= 12) {

                    loja.setPublicKey(publicKey);
                    loja.setAccessToken(accessToken);
                    loja.setParcelas(parcelas);

                    loja.salvar();

                } else {
                    binding.edtParcelas.requestFocus();
                    binding.edtParcelas.setError("Mínimo 1 e máximo 12.");
                }
            } else {
                binding.edtPublicKey.requestFocus();
                binding.edtPublicKey.setError("Informe seu access token.");
            }
        } else {
            binding.edtPublicKey.requestFocus();
            binding.edtPublicKey.setError("Informe sua public key.");
        }

        if (!nomeLoja.isEmpty()) {
            if (!CNPJ.isEmpty()) {
                if (CNPJ.length() == 18) {
                    if (!publicKey.isEmpty()) {
                        if (!accessToken.isEmpty()) {
                            if (parcelas > 0 && parcelas <= 12) {

                                ocultaTeclado();

                                loja.setNome(nomeLoja);
                                loja.setCNPJ(CNPJ);
                                loja.setPedidoMinimo(pedidoMinimo);
                                loja.setFreteGratis(freteGratis);
                                loja.setPublicKey(publicKey);
                                loja.setAccessToken(accessToken);
                                loja.setParcelas(parcelas);

                                if (caminhoImagem != null) {
                                    salvarImagemFirebase();
                                } else if (loja.getUrlLogo() != null) {
                                    loja.salvar();
                                } else {
                                    ocultaTeclado();
                                    Toast.makeText(this, "Escolha uma logo para sua empresa", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                binding.edtParcelas.requestFocus();
                                binding.edtParcelas.setError("Mínimo 1 e máximo 12");
                            }
                        } else {
                            binding.edtAcessToken.requestFocus();
                            binding.edtAcessToken.setError("Informe seu acess token");
                        }
                    } else {
                        binding.edtPublicKey.requestFocus();
                        binding.edtPublicKey.setError("Informe sua public key");
                    }
                } else {
                    binding.edtCNPJ.requestFocus();
                    binding.edtCNPJ.setError("CNPJ inválido");
                }
            } else {
                binding.edtCNPJ.requestFocus();
                binding.edtCNPJ.setError("Informe o CNPJ da sua empresa");
            }
        } else {
            binding.edtLoja.requestFocus();
            binding.edtLoja.setError("Informe um nome para a sua empresa");
        }

    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("loja")
                .child(loja.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            loja.setUrlLogo(task.getResult().toString());

            loja.salvar();

            caminhoImagem = null;

        })).addOnFailureListener(e -> Toast.makeText(
                this, "Ocorreu um erro com o upload, tente novamente.",
                Toast.LENGTH_SHORT).show());
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionlistener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?"
        );
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    Uri imagemSelecionada = result.getData().getData();
                    caminhoImagem = imagemSelecionada.toString();
                    binding.imgLogo.setImageBitmap(getBitmap(imagemSelecionada));
                }
            }
    );

    private Bitmap getBitmap(Uri caminhoUri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), caminhoUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtLoja.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
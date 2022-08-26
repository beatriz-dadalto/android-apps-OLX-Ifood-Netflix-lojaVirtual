package com.beatriz.controledeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FormProdutoActivity extends AppCompatActivity {

    private EditText editProduto;
    private EditText editQuantidade;
    private EditText editValor;

    private ProdutoDAO produtoDAO;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);

        produtoDAO = new ProdutoDAO(this);

        editProduto = findViewById(R.id.edit_produto);
        editQuantidade = findViewById(R.id.edit_quantidade);
        editValor = findViewById(R.id.edit_valor);

        // bundle para verificar se na intent tem dados recebido de outra activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // produto é a chave que coloquei na intent
            produto = (Produto) bundle.getSerializable("produto");

            editarProduto();
        }

    }

    private void editarProduto() {
        editProduto.setText(produto.getNome());
        editQuantidade.setText(String.valueOf(produto.getEstoque()));
        editValor.setText(String.valueOf(produto.getValor()));
    }


    public void salvarProduto(View view) {
        // pegar os inputs do user
        String nome = editProduto.getText().toString();
        String quantidade = editQuantidade.getText().toString();
        String valor = editValor.getText().toString();

        // validar
        if (!nome.isEmpty()) {
            if (!quantidade.isEmpty()) {
                int qtd = Integer.parseInt(quantidade);
                if (qtd >= 1) {
                     if (!valor.isEmpty()) {
                         double valorProduto = Double.parseDouble(valor);
                         if (valorProduto > 0) {
                             Toast.makeText(this, "Tudo certo", Toast.LENGTH_SHORT).show();

                             if (produto == null) {
                                 produto = new Produto();
                             }
                             produto.setNome(nome);
                             produto.setEstoque(qtd);
                             produto.setValor(valorProduto);

                             if (produto.getId() != 0) {
                                 produtoDAO.atualizarProduto(produto);
                             } else {
                                 produtoDAO.salvarProduto(produto);
                             }

                             // termina e volta pra tela de listagem
                             finish();

                         } else {
                             editValor.requestFocus();
                             editValor.setError("Informe um valor maior que zero");
                         }
                     } else {
                         editValor.requestFocus();
                         editValor.setError("Informe o valor do produto");
                     }
                } else {
                    editQuantidade.requestFocus();
                    editQuantidade.setError("Informe um valor maior que zero");
                }
            } else {
                editQuantidade.requestFocus();
                editQuantidade.setError("Informe um valor válido");
            }
        } else {
            editProduto.requestFocus();
            editProduto.setError("informe o nome do produto");
        }
    }


}
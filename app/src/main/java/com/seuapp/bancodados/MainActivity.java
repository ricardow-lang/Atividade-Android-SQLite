package com.seuapp.bancodados;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editPreco;
    private Button btnSalvar;
    private ListView listViewProdutos;
    private ProdutoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editPreco = findViewById(R.id.editPreco);
        btnSalvar = findViewById(R.id.btnSalvar);
        listViewProdutos = findViewById(R.id.listViewProdutos);

        dbHelper = new ProdutoDbHelper(this);

        btnSalvar.setOnClickListener(v -> salvarProduto());

        atualizarLista();
    }

    private void salvarProduto() {
        String nome = editNome.getText().toString().trim();
        String precoStr = editPreco.getText().toString().trim();

        if (nome.length() < 3) {
            Toast.makeText(this, "Nome inválido. Mínimo de 3 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoStr);
            if (preco <= 0) {
                Toast.makeText(this, "Preço inválido. Informe um valor maior que zero.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preço inválido. Digite um número.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean sucesso = dbHelper.inserirProduto(nome, preco);
        if (sucesso) {
            Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            editNome.setText("");
            editPreco.setText("");
            atualizarLista();
        } else {
            Toast.makeText(this, "Erro ao salvar produto.", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarLista() {
        List<String> produtos = dbHelper.listarProdutos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                produtos
        );
        listViewProdutos.setAdapter(adapter);
    }
}
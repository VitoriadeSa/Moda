package pt.ubi.di.pdm.modadebolsa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ubi.di.pdm.modadebolsa.data.AppDatabase;
import pt.ubi.di.pdm.modadebolsa.data.Molde;
import pt.ubi.di.pdm.modadebolsa.databinding.ActivityListaMoldesBinding;
import pt.ubi.di.pdm.modadebolsa.ui.MoldeAdapter;
import pt.ubi.di.pdm.modadebolsa.ui.NavegacaoInferior;

public class ListaMoldesActivity extends AppCompatActivity {

    public static final String EXTRA_ID_CATEGORIA = "idCategoria";
    public static final String EXTRA_NOME_CATEGORIA = "nomeCategoria";

    private ActivityListaMoldesBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaMoldesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavegacaoInferior.configurar(this, binding.bottomNav, R.id.nav_moldes);

        int idCategoria = getIntent().getIntExtra(EXTRA_ID_CATEGORIA, -1);
        String nomeCategoria = getIntent().getStringExtra(EXTRA_NOME_CATEGORIA);
        binding.tvTitulo.setText("Moldes de: " + nomeCategoria);

        MoldeAdapter adapter = new MoldeAdapter(molde -> {
            Intent i = new Intent(this, MoldeActivity.class);
            i.putExtra(MoldeActivity.EXTRA_ID_MOLDE, molde.idMolde);
            startActivity(i);
        });
        binding.rvMoldes.setAdapter(adapter);

        // Consulta à BD fora da thread principal
        executor.execute(() -> {
            List<Molde> moldes = AppDatabase.getInstance(this)
                    .moldeDao()
                    .obterPorCategoria(idCategoria);
            runOnUiThread(() -> adapter.definirLista(moldes));
        });


        EditText etPesquisa = findViewById(R.id.etPesquisa);
        etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                adapter.filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}